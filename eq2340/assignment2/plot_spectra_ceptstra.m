sampling_frequency_female = 44100;
sampling_frequency_male = 44100;
sampling_frequency_music = 22050;

female_wave = audioread('Sounds/female.wav');
male_wave = audioread('./Sounds/male.wav');
music_wave = audioread('./Sounds/music.wav');

win_length=0.03;
ncep=13;

[female_mfcc,female_spectr,f_f,f_t] = GetSpeechFeatures(female_wave',sampling_frequency_female,win_length, ncep);
% normalize MFCC
female_mfcc = female_mfcc - mean(female_mfcc,2);
female_mfcc = (female_mfcc' ./ std(female_mfcc'))';

figure();
ax1=subplot(2,1,1);
imagesc(log(female_spectr));
ax1.XTick=1:20:size(female_spectr,2);
ax1.XTickLabels=f_t(1:20:end);
ax1.YTick=1:100:size(female_spectr,1);
ax1.YTickLabels=f_f(1:100:end);
title('log spectrogram');
xlabel('time [s]');
ylabel('frequency [Hz]');
colorbar;

ax2=subplot(2,1,2);
imagesc(female_mfcc);
ax2.XTick=1:20:size(female_mfcc,2);
ax2.XTickLabels=f_t(1:20:end);
title('Normalized MFCC');
xlabel('time[s]');
ylabel('13 MFCC coefficients');
colorbar;

suptitle('female.wav');

[music_mfcc,music_spectr,music_f,music_t] = GetSpeechFeatures(music_wave',sampling_frequency_music,win_length, ncep);
% normalize MFCC
music_mfcc = music_mfcc - mean(music_mfcc,2);
music_mfcc = (music_mfcc' ./ std(music_mfcc'))';

figure();
ax1=subplot(2,1,1);
imagesc(log(music_spectr));
ax1.XTick=1:20:size(music_spectr,2);
ax1.XTickLabels=music_t(1:20:end);
ax1.YTick=1:100:size(music_spectr,1);
ax1.YTickLabels=music_f(1:100:end);
title('log spectrogram');
xlabel('time [s]');
ylabel('frequency [Hz]');
colorbar;


ax2=subplot(2,1,2);
imagesc(music_mfcc);
ax2.XTick=1:20:size(music_mfcc,2);
ax2.XTickLabels=music_t(1:20:end);
title('Normalized MFCC');
xlabel('time[s]');
ylabel('13 MFCC coefficients');
colorbar;

suptitle('music.wav');


[male_mfcc,male_spectr,male_f,male_t] = GetSpeechFeatures(male_wave',sampling_frequency_male,win_length, ncep);
% normalize MFCC
male_mfcc = male_mfcc - mean(male_mfcc,2);
male_mfcc = (male_mfcc' ./ std(male_mfcc'))';

figure();
ax1=subplot(2,1,1);
imagesc(log(male_spectr));
ax1.XTick=1:20:size(male_spectr,2);
ax1.XTickLabels=male_t(1:20:end);
ax1.YTick=1:100:size(male_spectr,1);
ax1.YTickLabels=male_f(1:100:end);
title('log spectrogram');
xlabel('time [s]');
ylabel('frequency [Hz]');
colorbar;


ax2=subplot(2,1,2);
imagesc(male_mfcc);
ax2.XTick=1:20:size(male_mfcc,2);
ax2.XTickLabels=male_t(1:20:end);
title('Normalized MFCC');
xlabel('time[s]');
ylabel('13 MFCC coefficients');
colorbar;

suptitle('male.wav');
