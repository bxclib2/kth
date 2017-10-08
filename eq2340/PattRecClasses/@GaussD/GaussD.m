%GaussD - Probability distribution class, representing
%Gaussian random vector
%EITHER with statistically independent components,
%           i.e. diagonal covariance matrix, with zero correlations,
%OR with a full covariance matrix, including correlations
%-----------------------------------------------------------------------
%
%Several GaussD objects may be collected in a multidimensional array,
%           even if they do not have the same DataSize.
%
%Arne Leijon, 2009-07-21
%             2010-11-21, minor cleanup

classdef GaussD < ProbDistr
    properties (Dependent)
        DataSize;%      length of random vectors
    end
    properties (GetAccess=public,SetAccess=public)
        Mean=0;%        mean of random vector
        Variance;%      variance of each vector element, =diag(Covariance)
        StDev=1;%       sqrt of Variance
        Covariance;%    full covariance matrix
        AllowCorr;%     =TRUE, if full covariance matrix is allowed,
        %               even if correlations are actually zero.
        %               =FALSE (default), if covariance matrix is forced to remain diagonal.
    end
    properties (GetAccess=private,SetAccess=private)
        %***** Internal representation:
        %gD.Mean=   column vector with mean value
        %gD.StDev=  column vector with sqrt of covariance eigenvalues
        CovEigen=1;%    matrix (W) with covariance eigenvectors stored columnwise, 
        %               if full covariance matrix is allowed.
        %           =   scalar 1, if diagonal covariance matrix is enforced.
        %So far, CovEigen is always square, but a PCA implementation
        %might also allow fewer CovEigen column eigenvectors.
        %Then, we also need   
        %ResidStDev=    scalar st.Dev of residual random variable,
        %               in addition to Principal-component subspace
    end
    methods (Access=public) 
        function gD = GaussD(varargin)
            %Constructor for a single GaussD object
            %***Usage:
            %gD = GaussD; 
            %   creates a single std normal N(0,1) distribution.
            %gD = GaussD(pD); 
            %   just copies the given object.
            %gD = GaussD(propertyName,propertyValue,...etc.)
            %   creates a GaussD object with given class properties.
            %
            switch nargin
                case 0;%default scalar normalized Gaussian random variable
                case 1
                    pD=varargin{1};
                    if isa(pD,'GaussD')
                        gD= pD;%copy it without transformation
                    else
                        error('Argument must be a GaussD object');
                    end;
                otherwise
                     gD=setproperties(gD,varargin{:});%and set all its properties
             end;%switch
        end
        %
        %***** Methods for General Use:
        R=rand(pD,nData);%      generate random vector samples
        %[p,logS]=prob(pD,x);%   Superclass: probability densities for observed vectors
        logP=logprob(pD,x);%    log(probability densities) for observed vector samples
        %c=cov(pD);%            *** use pD.Covariance instead
        plotH=plotCross(pD,proj,colors);%   2D plot of main axes of distribution ellipsoid
        %****plotProb  3D plot of probability density function
        %
        %***** Training Methods:
        %NOTE: These training methods can be used also to adapt several GaussD objects,
        %automatically assigning training vectors to different objects.
        %See adaptAccum for details.
        %
        [pD,iOK]=init(pD,x);%   initialize crudely to conform with given data
        aState=adaptStart(pD);% initialize accumulator data structure for data statistics
        aState=adaptAccum(pD,aState,obsData,obsWeight);
        %       collect sufficient statistics from observed data vectors,
        %       without changing the object itself
        %       (may be called repeatedly with different data subsets).
        pD=adaptSet(pD,aState);%    finally adjust the object using accumulated statistics.
        %       The covariance matrix remains diagonal,
        %       if previously initialized via properties StDev or Variance.
        %
    end
    methods%***** set/get methods
        %Note:      If properties StDev or Variance are set,
        %           property AllowCorr is NOT changed, only
        %           the covariance eigenvalues.
        %           Thus, the main axes of a correlated distribution
        %           are NOT changed.
        %
        %           If property Covariance is set, 
        %           a full covariance matrix is allowed,
        %           including non-zero correlations, i.e.
        %           property AllowCorr is set to TRUE.
        %
        function D=get.DataSize(gD)
            D=length(gD.Mean);
        end
        function v=get.Variance(gD)
            v=gD.StDev.^2;
        end
        function gD=set.Variance(gD,v)
            gD.StDev=sqrt(abs(v));
        end
        function C=get.Covariance(gD)
            C=gD.CovEigen*diag(gD.StDev.^2)*gD.CovEigen';
        end
        function gD=set.Covariance(gD,c)
            gD=setCov(gD,c);
        end
        function ac=get.AllowCorr(gD)
            ac=allowsCorr(gD);
        end
        function gD=set.AllowCorr(gD,ac)
            if ac%allow full cov matrix
               if ~allowsCorr(gD)%use existing StDev for full cov matrix
                    gD.CovEigen=eye(length(gD.Mean));%id matrix
               end;%else already set, no change
            else%enforce diagonal cov matrix
                if allowsCorr(gD)%change to NOT allow Corr
                    gD.CovEigen=1;%force diagonal covariance matrix;
                end;%else no change
            end;
        end
    end
    methods (Access='private')
        function C=allowsCorr(pD)%check if full cov matrix is allowed
            pDsize=size(pD);%size of GaussD array
            nObj=prod(pDsize);
            C=zeros(pDsize);
            for i=1:nObj
                C(i)=~all(size(pD(i).CovEigen) == 1);
            end;
        end
        function pD=setCov(pD,c)
        %pD=setCov(pD,c) sets internal variables
        %in a given single GaussD object,
        %to conform with given covariance matrix.
        %
        %Input:
        %pD=    GaussD object
        %c=     full square covariance matrix
        %
        %Result:
        %pD=    GaussD object, with correctly set internal properties
        %       StDev= sqrt of eigenvalues of covariance matrix, and
        %       CovEigen= eigenvectors of covariance matrix
        %  
        %Arne Leijon 2005-11-16 tested
        %           2006-04-27 checked for slightly neg. eigenvalues...
            if size(c,1)~=size(c,2)
                error('Covariance must be square');end;
            % minc=min(min(c));
            % if minc<0
            %     warning(['Neg. covariance element:',num2str(minc)]);end;
            if ~isreal(c)
                error('Covariance must be real-valued');end;
            if max(max(abs(c-c')))<1E-4*max(max(abs(c+c')))%and symmetric
                [pD.CovEigen,v]=eig(0.5*(c+c'));%force symmetric covariance!
                %check for small imag eigenvector components:
                maxImag=max(abs(imag(pD.CovEigen(:))));
                if ~isreal(pD.CovEigen)%should not happen for symmetric covariance!!!
                    warning('GaussD:setCov',['Cov. eigenvectors forced to real. Max error:',num2str(maxImag)]);
                    pD.CovEigen=real(pD.CovEigen);
                end;

                pD.StDev=sqrt(abs(diag(v)));
            %abs() was used above, because
            %zero-valued eigenvalues may be slightly negative,
            %because of limited precision,
            %although true eigenvalues are of course always positive or zero.

            else
                error('Covariance must be symmetric');
            end;
        end
        function [pD,OK]=setproperties(pD,varargin)
        %[pD,OK]=setproperties(pD,propName,value,...) sets value of property propName
        %for a single GaussD object or array of GaussD objects
        %(Mainly for backward compatibility)
        %
        %Input:
        %pD=    the object or object array
        %propName, value=   property name, property value
        %If value is a numeric array,
        %       ALL elements in the object array pD are assigned the new property value.
        %If value is a cell array, it must have the same size as the GaussD array,
        %       and then each object is assigned with the corresponding value cell.
        %
        %Several (propName,value) argument pairs may follow.
        %
        %Note:      If properties StDev or Variance are set,
        %           property AllowCorr is NOT changed, only
        %           the covariance eigenvalues.
        %           Thus, the main axes of a correlated distribution
        %           are NOT changed.
        %
        %           If property Covariance is set, 
        %           a full covariance matrix is allowed,
        %           including non-zero correlations, i.e.
        %           property AllowCorr is set to TRUE.
        %
        %Result:
        %pD=    modified object(s)
        %OK=    TRUE, if properties are acceptable in all pD elements
        %
        %Arne Leijon 2007-02-01 tested
            sizObj=size(pD);%object array size
            nObj=prod(sizObj);%number of objects in array
            property_argin = varargin;
            while length(property_argin) >= 2,
                propName = property_argin{1};
                v = property_argin{2};
                property_argin = property_argin(3:end);
                if iscell(v)
                    if any(sizObj~=size(v))
                        error([propName,' cell array has incompatible size']);end;
                end;
                switch propName
%                     case 'DataSize'
%                         warning('DataSize is specified only by Mean property');
                    case 'Mean'
                        if iscell(v)
                            for i=1:nObj
                                pD(i).Mean=v{i}(:);%each object gets separate value
                            end;
                        else
                            for i=1:nObj
                                pD(i).Mean=v(:);%all objects get same value
                            end;
                        end;
                    case 'StDev'
                        if iscell(v)
                            for i=1:nObj
                                pD(i).StDev=abs(v{i}(:));%each object gets separate value
                            end;
                        else
                            for i=1:nObj
                                pD(i).StDev=abs(v(:));%all objects get same value
                            end;
                        end;
                    case 'Variance'
                        if iscell(v)
                            for i=1:nObj
                                pD(i).StDev=sqrt(abs(v{i}(:)));%each object gets separate value
                            end;
                        else
                            for i=1:nObj
                                pD(i).StDev=sqrt(abs(v(:)));%all objects get same value
                            end;
                        end;
                    case 'Covariance'
                        if iscell(v)
                            for i=1:nObj
                                pD(i)=setCov(pD(i),v{i});%each object gets separate value
                            end;
                        else
                            for i=1:nObj
                                pD(i)=setCov(pD(i),v);%each object gets same value
                            end;
                        end;
                    case 'UserData'
                        if iscell(v)
                            for i=1:nObj
                                pD(i).UserData=v{i};%each object gets separate value
                            end;
                        else
                            for i=1:nObj
                                pD(i).UserData=v;%all objects get same value
                            end;
                        end;
                    case 'AllowCorr'
                        if all(size(v)==1)%single value, each object gets same value
                            if v==1%TRUE, set full covariance
                                for i=1:nObj
                                    if ~allowsCorr(pD(i))%use StDev for full cov matrix
                                        pD(i).CovEigen=eye(length(pD(i).Mean));%id matrix
                                    end;%else already set, no change
                                end;
                            else%force diagonal
                                for i=1:nObj
                                    if allowsCorr(pD(i))%set to NOT allow Corr
                                        pD(i).CovEigen=1;%force diagonal covariance matrix;
                                    end;%else no change
                                end;
                            end;
                        else
                            if any(sizObj~=size(v))
                                error([propName,' data has incompatible size']);end;
                            for i=1:nObj
                                if v(i)==1%TRUE, set full covariance
                                    if ~allowsCorr(pD(i))%set it
                                        pD(i).CovEigen=eye(length(pD(i).Mean));%id matrix
                                    end;%else already set, no change
                                else%force diagonal
                                    if allowsCorr(pD(i))%set to NOT allow Corr
                                        pD(i).CovEigen=1;%force diagonal covariance matrix;
                                    end;%else no change
                                end;                       
                            end;
                        end;
                    otherwise
                        warning('GaussD:CannotSet',['Cannot set property ',propName,' of this object']);
                end;%switch
            end;%while
            OK=propertiesOK(pD);%check all properties    
        end
        function OK=propertiesOK(gD)%check consistency of properties
            nObj=numel(gD);
            OK=true;
            for i=1:nObj
                if isempty(gD(i).Mean)
                    OK=false;
                    warning('GaussD:propCheck','No Mean value');return;
                end;%cannot check further
                if isempty(gD(i).StDev)
                    OK=false;
                    warning('GaussD:propCheck','No StDev value');return;
                end;%cannot check further
                if length(gD(i).StDev) ~= length(gD(i).Mean)
                    OK=false;
                    warning('GaussD:propCheck','Mean and Covariance have incompatible size');
                end;
            end; 
        end
    end
end
