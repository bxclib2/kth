sampling_frequency_female = 44100;
sampling_frequency_male = 44100;

female_wave = audioread('./Sounds/female.wav');
male_wave = audioread('./Sounds/male.wav');

win_length=0.03;
ncep=13;

% Plot spectrograms
[female_mfcc,female_spectr,f_f,f_t] = GetSpeechFeatures(female_wave',sampling_frequency_female,win_length,ncep);
%figure();
%ax1=subplot(2,1,1);
%imagesc(log(female_spectr));
%ax1.XTick=1:20:size(female_spectr,2);
%ax1.XTickLabels=f_t(1:20:end);
%ax1.YTick=1:100:size(female_spectr,1);
%ax1.YTickLabels=f_f(1:100:end);
%title('female.wav');
%xlabel('time [s]');
%ylabel('frequency [Hz]');
%colorbar;


[male_mfcc,male_spectr,male_f,male_t] = GetSpeechFeatures(male_wave',sampling_frequency_male,win_length,ncep);
%ax2=subplot(2,1,2);
%imagesc(log(male_spectr));
%ax2.XTick=1:20:size(male_spectr,2);
%ax2.XTickLabels=male_t(1:20:end);
%ax2.YTick=1:100:size(male_spectr,1);
%ax2.YTickLabels=male_f(1:100:end);
%title('male.wav');
%xlabel('time [s]');
%ylabel('frequency [Hz]');
%colorbar;
%
%subtitle('log spectrograms');

% Plot MFCCs
%figure();
%ax1=subplot(2,1,1);
%imagesc(female_mfcc);
%ax1.XTick=1:20:size(female_mfcc,2);
%ax1.XTickLabels=f_t(1:20:end);
%title('female.wav');
%xlabel('time [s]');
%ylabel('13 MFCC coefficients');
%colorbar;
%
%ax2=subplot(2,1,2);
%imagesc(male_mfcc);
%ax2.XTick=1:20:size(male_mfcc,2);
%ax2.XTickLabels=male_t(1:20:end);
%title('male.wav');
%xlabel('time [s]');
%ylabel('13 MFCC coefficients');
%colorbar;


% Plot MFCC correlation
figure();
subplot(2,1,1);
imagesc(corr(female_mfcc'));
colormap gray;
colorbar;
title('female.wav');

subplot(2,1,2);
imagesc(corr(male_mfcc'));
colormap gray;
colorbar;
title('male.wav');

suptitle('MFCC correlation');


% Plot spectrogram correlation
figure();
subplot(2,1,1);
imagesc(abs(corr(female_spectr')));
colormap gray;
colorbar;
title('female.wav');

subplot(2,1,2);
imagesc(abs(corr(male_spectr')));
colormap gray;
colorbar;
title('male.wav');

suptitle('Spectrogram correlation');
