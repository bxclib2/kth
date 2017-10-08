sampling_frequency_three = 44100;
sampling_frequency_free = 44100;

three_wave = audioread('./Sounds/three.wav');
free_wave = audioread('./Sounds/free.wav');

win_length=0.03;
ncep=13;

[three_mfcc,three_spectr,f_f,f_t] = GetSpeechFeatures(three_wave',sampling_frequency_three,win_length, ncep);
% normalize MFCC
three_mfcc = three_mfcc - mean(three_mfcc,2);
three_mfcc = (three_mfcc' ./ std(three_mfcc'))';

figure();
ax1=subplot(2,1,1);
% imagesc(log(three_spectr));
% ax1.XTick=1:20:size(three_spectr,2);
% ax1.XTickLabels=f_t(1:20:end);
% ax1.YTick=1:100:size(three_spectr,1);
% ax1.YTickLabels=f_f(1:100:end);
% title('log spectrogram');
% xlabel('time [s]');
% ylabel('frequency [Hz]');
% colorbar;

% ax2=subplot(2,1,2);
imagesc(three_mfcc);
ax2.XTick=1:20:size(three_mfcc,2);
ax2.XTickLabels=f_t(1:20:end);
title('Normalized MFCC (three)');
xlabel('time[s]');
ylabel('13 MFCC coefficients');
colorbar;

%suptitle('three.wav');

[free_mfcc,free_spectr,free_f,free_t] = GetSpeechFeatures(free_wave',sampling_frequency_free,win_length, ncep);
% normalize MFCC
free_mfcc = free_mfcc - mean(free_mfcc,2);
free_mfcc = (free_mfcc' ./ std(free_mfcc'))';

%figure();
% ax1=subplot(2,1,1);
% imagesc(log(free_spectr));
% ax1.XTick=1:20:size(free_spectr,2);
% ax1.XTickLabels=free_t(1:20:end);
% ax1.YTick=1:100:size(free_spectr,1);
% ax1.YTickLabels=free_f(1:100:end);
% title('log spectrogram');
% xlabel('time [s]');
% ylabel('frequency [Hz]');
% colorbar;


ax2=subplot(2,1,2);
imagesc(free_mfcc);
ax2.XTick=1:20:size(free_mfcc,2);
ax2.XTickLabels=free_t(1:20:end);
title('Normalized MFCC (free)');
xlabel('time[s]');
ylabel('13 MFCC coefficients');
colorbar;

%suptitle('free.wav');
