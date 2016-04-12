FROM debian:jessie
MAINTAINER Romaric Philogene <rphilogene@nousmotards.com>

RUN echo "deb http://ftp.fr.debian.org/debian jessie main non-free contrib" > /etc/apt/sources.list
RUN apt-get update && apt-get -y install \
maven2 wget curl unzip; apt-get clean

RUN echo "deb http://ppa.launchpad.net/webupd8team/java/ubuntu precise main" | tee /etc/apt/sources.list.d/webupd8team-java.list
RUN echo "deb-src http://ppa.launchpad.net/webupd8team/java/ubuntu precise main" | tee -a /etc/apt/sources.list.d/webupd8team-java.list
RUN apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv-keys EEA14886
RUN echo debconf shared/accepted-oracle-license-v1-1 select true | debconf-set-selections && \
echo debconf shared/accepted-oracle-license-v1-1 seen true | debconf-set-selections
RUN apt-get update && apt-get install -y oracle-java8-installer oracle-java8-set-default; apt-get clean

ENV JAVA_HOME /usr/lib/jvm/java-8-oracle

RUN cd /usr/local/bin && wget https://services.gradle.org/distributions/gradle-2.4-all.zip && \
/usr/bin/unzip gradle-2.4-all.zip && ln -s /usr/local/bin/gradle-2.4/bin/gradle /usr/bin/gradle