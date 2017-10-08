sampling_frequency_female = 44100;
sampling_frequency_music = 22050;

female_wave = audioread('./Sounds/female.wav');
music_wave = audioread('./Sounds/music.wav');

win_length=0.03;

[f_spectrogram,f_f,f_t] = GetSpeechFeatures(female_wave',sampling_frequency_female,win_length);
figure();
subplot(2,1,1);
imagesc(log(f_spectrogram));
annotation('rectangle', [0.14 0.87 0.5 0.05], 'Color', 'red')
set(gca,'XTick',1:20:size(f_spectrogram,2));
set(gca,'XTickLabels',f_t(1:20:end));
set(gca,'YTick',1:100:size(f_spectrogram,1));
set(gca,'YTickLabels',f_f(1:100:end));
title('female.wav, log spectrogram');
xlabel('time [s]');
ylabel('frequency [Hz]');
colorbar;

[music_spectrogram,music_f,music_t] = GetSpeechFeatures(music_wave',sampling_frequency_music,win_length);
subplot(2,1,2);
imagesc(log(music_spectrogram));
annotation('rectangle', [0.14 0.40 0.5 0.05], 'Color', 'red')
set(gca,'XTick',1:70:size(music_spectrogram,2));
set(gca,'XTickLabels',music_t(1:70:end));
set(gca,'YTick',1:100:size(music_spectrogram,1));
set(gca,'YTickLabels',music_f(1:100:end));
title('music.wav, log spectrogram');
xlabel('time [s]');
ylabel('frequency [Hz]');
colorbar;
