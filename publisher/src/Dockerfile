# https://mcr.microsoft.com/v2/dotnet/sdk/tags/list
FROM mcr.microsoft.com/dotnet/sdk:5.0.301 AS build
WORKDIR /app
COPY publisher.csproj ./
WORKDIR /app
RUN dotnet restore publisher.csproj -r linux-musl-x64
COPY / .

FROM build AS publish
WORKDIR /app
RUN dotnet publish publisher.csproj -r linux-musl-x64 --self-contained true -p:PublishTrimmed=True -p:TrimMode=Link -c release -o out --no-restore

# https://mcr.microsoft.com/v2/dotnet/runtime-deps/tags/list
FROM mcr.microsoft.com/dotnet/runtime-deps:5.0.7-alpine3.13-amd64
# gRPC issue: https://github.com/grpc/grpc/issues/21446
RUN echo 'http://dl-cdn.alpinelinux.org/alpine/v3.8/main' >> /etc/apk/repositories && apk update --no-cache && apk add --no-cache bash libc6-compat=1.1.19-r11
WORKDIR /app
COPY --from=publish /app/out ./
EXPOSE 7070
ENV COMPlus_EnableDiagnostics=0
USER 1000
ENTRYPOINT ["/app/publisher"]