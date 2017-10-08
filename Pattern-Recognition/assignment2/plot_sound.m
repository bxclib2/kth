sampling_frequency_female = 44100;
sampling_frequency_music = 22050;

female_wave = audioread('./Sounds/female.wav');
music_wave = audioread('./Sounds/music.wav');

female_time = (1:size(female_wave,1)) / sampling_frequency_female;
figure();
subplot(2,1,1);
plot(female_time, female_wave)
title('female.wav');
ylabel('audio signal');
xlabel('time [s]');

music_time = (1:size(music_wave,1)) / sampling_frequency_music;
subplot(2,1,2);
plot(music_time, music_wave)
title('music.wav');
ylabel('audio signal');
xlabel('time [s]');

%TODO snippets
time_window=0.02; % 20ms
time_steps=[0.5 1. 1.5];
figure();
for i=1:length(time_steps)
    time=time_steps(i);

    subplot(3,2,2*i-1);
    plot(female_time, female_wave);
    title('female.wav');
    ylabel('audio signal');
    xlabel('time [s]');
    axis([time time+time_window -0.2, 0.2]);

    subplot(3,2,2*i);
    plot(music_time, music_wave);
    title('music.wav');
    ylabel('audio signal');
    xlabel('time [s]');
    axis([time time+time_window -0.2, 0.2]);

end
