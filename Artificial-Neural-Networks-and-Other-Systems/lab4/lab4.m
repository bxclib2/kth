pict;
w = p1'*p1 + p2'*p2 + p3'*p3;% + p4'*p4;

%update(w, p111, 2000);

whos w
whos p1

p = p11;
E = energy(w,p);
whos E
display(E)

w = randn(1024);
%w = 0.5 * (w+w');
whos w
%p = sgn(diag(randn(1024))')
whos p

%figure;
%p1dist = flip(p1, 1024);
%vis(p1dist);
%waitforbuttonpress();

pdist = p1;
s = size(pdist,2);
range = [0.1:0.1:1];

% 5.2
% pdist = p22;
% subplot(1,2,1);
% vis(pdist);
% for i = 1:1000
%     random = 1 + floor(rand()*size(w, 1));
%     pdist(random) = sgn(w(random,:)*p22')';
% end
% subplot(1,2,2);
% vis(pdist);

%filename = sprintf('noise-%.0f%%.jpg', r*100);
%saveas(fig,filename);
waitforbuttonpress();

fig = figure();
for r  = range
    noise = round(r*s);
    pdist = flip(p1,noise); 
    subplot(1,2,1);
    vis(pdist);
    for i = 1:10000
        random = 1 + floor(rand()*size(w, 1));
        pdist(random) = sgn(p11*w(random,:)');
    end
    subplot(1,2,2);
    vis(pdist);
    fprintf('noise: %.0f%%\n', r * 100);
    
    %filename = sprintf('noise-%.0f%%.jpg', r*100);
    %saveas(fig,filename);
    waitforbuttonpress();
end

%for i = 1:2000
%    p = update(w, p);
%    E = energy(w,p);
%    m = mod(i, 100);
%    if m == 0
%        disp(E);
%        imagesc(reshape(p,32,32));
%        waitforbuttonpress();
%    end
%endfor


