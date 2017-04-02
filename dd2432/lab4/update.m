function p = update(w, p)
    %for i = 1:epochs
    %    r = 1 + floor(rand()*size(W, 1));
    %    p(r) = sgn(W(r,:)*p')';
    %    m = mod(i, 100)
    %    disp(m)
    %    if m == 0
    %        fh = figure;
    %        vis(p);
    %        waitfor(fh);       
    %    end
    %endfor
    r = 1 + floor(rand()*size(w, 1));
    p(r) = sgn(w(r,:)*p')';
