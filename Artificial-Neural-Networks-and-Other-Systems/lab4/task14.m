% capacity part 5 and a half and 6 and a half

close all;
%clearvars;
pict;

p = 0.01;
patt = rand(300,100);
patterns = zeros(300,100);

patterns(patt<=p) = 1;

% humne koshish ki thi 0.1 rakhne ki
real_p = mean(patterns(:));

numNodes = length(patterns(1,:));

% this needs to change
w = (patterns(1,:)-real_p)'*(patterns(1,:)-real_p); 


theta = 0:1:10;

l_theta = length(theta);
rat = zeros(l_theta,300);
for j = 1:l_theta
for ind = 2:300
    p1 = patterns(ind, :);
    w = w+(p1-real_p)'*(p1-real_p);
%     w = w - diag(diag(w));
    sm = 0;
    for ind1 = 1:ind-1
        patNoise = flip(patterns(ind1, :),5);
%         patNoise = patterns(ind1, :);
        
        x1_prev = patterns(ind1, :);
        x1_ret = zeros([1, numNodes]);
        error = numNodes;
        iter = 0;
        while(error~=0 && iter<15)
%             for i = 1:numNodes
                % can this be vectorized??
                x1_ret = 0.5 + 0.5*sign(x1_prev*w'-theta(j));
%                 x1_ret(i) = sgn(sum(x1_prev.*w(i, :)));
%             end
            error = sum(abs(x1_ret-x1_prev));
            x1_prev = x1_ret;
            iter = iter+1;
        end
        
        er = sum(abs(x1_ret-patterns(ind1, :)));
        sm = sm+(er==0);
    end
    rat(j,ind) = sm/length(1:ind-1);
end
w = (patterns(1,:)-real_p)'*(patterns(1,:)-real_p); 
end
% figure, plot(rat)
% errFirst = (find(rat<0.5));
% errFirst(2)

for i = 1:l_theta
if(~isempty(find(rat(i,:)>0)))
    figure, plot(rat(i,:)), title(sprintf('%0.3f',theta(i)))
end
end
