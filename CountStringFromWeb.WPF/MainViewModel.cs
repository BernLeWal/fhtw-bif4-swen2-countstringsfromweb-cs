using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading;
using System.Threading.Tasks;
using System.Windows;
using GalaSoft.MvvmLight;
using GalaSoft.MvvmLight.Command;

namespace CountStringFromWeb.WPF
{
    public class MainViewModel : ViewModelBase
    {
        public RelayCommand GetDataCommand { get; }
        public RelayCommand GetDataAsyncCommand { get; }
        public RelayCommand GetDataAsyncSynchronizedCommand { get; }

        public RelayCommand ExitCommand { get; }

        public BindingList<string> Stories { get; } = new BindingList<string>();

        private string _status;
        public string Status
        {
            get { return _status; }
            set { this.Set(ref _status, value, nameof(Status)); }
        }

        private SemaphoreSlim _getDataSemaphore = new SemaphoreSlim(1, 1);

        public MainViewModel()
        {
            // https://docs.microsoft.com/de-de/dotnet/csharp/programming-guide/concepts/async/task-asynchronous-programming-model
            GetDataCommand = new RelayCommand(GetData);
            GetDataAsyncCommand = new RelayCommand(async () => await GetDataAsync());
            GetDataAsyncSynchronizedCommand = new RelayCommand(async () => await GetDataAsyncSynchronized());
            ExitCommand = new RelayCommand(Exit);
        }

        private void GetData()
        {
            Status = "Starting sync get data at " + DateTime.Now.ToLongTimeString();
            Stories.Clear();

            var webClient = new System.Net.WebClient();
            string content = webClient.DownloadString("https://sport.orf.at");
            Thread.Sleep(30_000);
            var findStoriesRegex = new Regex("ticker-story-headline.*?a href.*?>(.*?)<\\/a>",
                RegexOptions.IgnoreCase | RegexOptions.Singleline);
            var matches = findStoriesRegex.Matches(content);
            foreach (Match match in matches)
            {
                Stories.Add(match.Groups[1].ToString().Trim());
            }
        }

        private async Task GetDataAsync()
        {
            Status = "Starting async get data at " + DateTime.Now.ToLongTimeString();
            await Task.Run(async () =>
            {
                Application.Current.Dispatcher.Invoke(() => { Stories.Clear(); });

                var webClient = new System.Net.WebClient();
                Task<string> contentTask = webClient.DownloadStringTaskAsync("https://sport.orf.at");
                await Task.Delay(30_000);
                var findStoriesRegex = new Regex("ticker-story-headline.*?a href.*?>(.*?)<\\/a>",
                    RegexOptions.IgnoreCase | RegexOptions.Singleline);
                var matches = findStoriesRegex.Matches(await contentTask);
                Application.Current.Dispatcher.Invoke(() =>
                {
                    foreach (Match match in matches)
                    {
                        Stories.Add(match.Groups[1].ToString().Trim());
                    }
                });
            });
        }
        private async Task GetDataAsyncSynchronized()
        {
            await _getDataSemaphore.WaitAsync();
            try
            {
                await GetDataAsync();
            }
            finally
            {
                _getDataSemaphore.Release();
            }
        }

        private void Exit()
        {
            // this is not a proper solution, but for the moment it is fine
            // Problem: we are mixing WPF code here into the WPF-less code
            Application.Current.MainWindow.Close();

            // for details,
            // see: https://stackoverflow.com/questions/16172462/close-window-from-viewmodel
            //      https://social.msdn.microsoft.com/Forums/vstudio/en-US/48fad9a7-950e-4a18-a09a-57e7292fa516/close-hide-move-borderless-window-without-passing-window-instance-mvvm-light?forum=wpf
        }
    }
}
