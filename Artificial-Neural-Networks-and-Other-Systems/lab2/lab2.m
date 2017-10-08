close all;
x = [0:0.1:2*pi]';
f = sqrt(x) + cos(2*x)
%f = square(2*x);
units = 52;
makerbf;
Phi = calcPhi(x,m,var);
whos Phi
w = Phi\f;
y = Phi*w;
whos x
whos y
whos f
rbfplot1(x, y, f, units);

% units=63;
% makerbf;
% fun = 'sin2x';
% eta = 1.5;
% itermax=20000;
% diter

% plotinit;
% % 
% data=read('cluster');
% units=5;
% singlewinner=1;
% makerbf;
% vqinit;
% 
% vqiter;
%emiterb;

% [xtrain, ytrain] = readxy('ballist', 2, 2);
% [xtest, ytest ] = readxy('balltest', 2, 2);
% 
% % lowpass = find(xtrain > 0.01)
% % xtrain = xtrain(lowpass);
% % ytrain = ytrain(lowpass);
% 
% units = 20;
% data = xtrain;
% vqinit;
% singlewinner=1;
% emiterb;
% 
% Phi=calcPhi(xtrain,m,var);
% d1=ytrain(:,1);
% d2=ytrain(:,2);
% dtest1=ytest(:,1)
% dtest2=ytest(:,2)
% 
% w1=Phi\d1;
% w2=Phi\d2;
% 
% y1=Phi*w1;
% y2=Phi*w2;
% 
% Phitest=calcPhi(xtest,m,var);
% ytest1=Phitest*w1;
% ytest2=Phitest*w2;
% 
% xyplot(d1,y1,'train1');
% xyplot(d2,y2,'train2');
% xyplot(dtest1,ytest1,'test1');
% xyplot(dtest2,ytest2,'test2');

