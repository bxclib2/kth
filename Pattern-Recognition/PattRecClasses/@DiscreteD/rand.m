function R=rand(pD,nData)
%R=rand(pD,nData) returns random scalars drawn from given Discrete Distribution.
%
%Input:
%pD=    DiscreteD object
%nData= scalar defining number of wanted random data elements
%
%Result:
%R= row vector with integer random data drawn from the DiscreteD object pD
%   (size(R)= [1, nData]
%
%----------------------------------------------------
%Code Authors:
%----------------------------------------------------

if numel(pD)>1
    error('Method works only for a single DiscreteD object');
end;

%*** Insert your own code here and remove the following error message 
R = zeros(1, nData);
for n = 1:nData
    choice = rand(1);
    for i = 1:length(pD.ProbMass)
        if choice <= sum(pD.ProbMass(1:i))
            R(1,n) = i;
            break;
        end
    end
end
