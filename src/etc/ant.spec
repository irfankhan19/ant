%define	packname    jakarta-ant
%define	applibdir   /usr/share/ant
%define dist_tar    @DIST_TAR@

Summary: A Java based build tool.
Name: ant
Version: @VERSION@
Release: @RPM_RELEASE@
Group: Development/Tools
Copyright: Apache Software License
Provides: ant
Url: http://jakarta.apache.org/ant
BuildArch: noarch
Source: http://jakarta.apache.org/builds/jakarta-ant/@RPM_SOURCE@/src/%{packname}-%{version}-src.tar.gz
BuildRoot: /var/tmp/ant-root
Vendor: Apache Software Foundation
Packager: Apache Software Foundation

%description
Apache Ant is a platform-independent build tool implemented in Java.
It is used to build a number of projects including the Apache Jakarta 
and XML projects.

%prep

%build

%install
mkdir -p $RPM_BUILD_ROOT/%{applibdir}
cd $RPM_BUILD_ROOT
tar zxvf %{dist_tar}
cp -r $RPM_BUILD_ROOT/%{packname}-%{version}/bin $RPM_BUILD_ROOT/%{applibdir}
cp -r $RPM_BUILD_ROOT/%{packname}-%{version}/lib $RPM_BUILD_ROOT/%{applibdir}
cp -r $RPM_BUILD_ROOT/%{packname}-%{version}/docs $RPM_BUILD_DIR 
cp -r $RPM_BUILD_ROOT/%{packname}-%{version}/LICENSE $RPM_BUILD_DIR 
cp -r $RPM_BUILD_ROOT/%{packname}-%{version}/README $RPM_BUILD_DIR 
cp -r $RPM_BUILD_ROOT/%{packname}-%{version}/KEYS $RPM_BUILD_DIR
cp -r $RPM_BUILD_ROOT/%{packname}-%{version}/WHATSNEW $RPM_BUILD_DIR

%clean
[ "$RPM_BUILD_ROOT" != "/" ] && rm -rf $RPM_BUILD_ROOT

%post

%preun
  
%files
%defattr(-,root,root)
%doc LICENSE README WHATSNEW KEYS
%doc docs
%{applibdir}/lib
%{applibdir}/bin


%changelog
