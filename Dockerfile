# https://mcr.microsoft.com/v2/dotnet/sdk/tags/list
FROM mcr.microsoft.com/dotnet/sdk:5.0.203 AS build
WORKDIR /app
COPY src/pubsubpublisher.csproj ./src/
WORKDIR /app/src
RUN dotnet restore pubsubpublisher.csproj -r linux-musl-x64
COPY src/ .

FROM build AS publish
WORKDIR /app/src
# Fix the issue on Debian 10: https://github.com/dotnet/dotnet-docker/issues/2470
ENV COMPlus_EnableDiagnostics=0
RUN dotnet publish pubsubpublisher.csproj -p:PublishSingleFile=true -r linux-musl-x64 --self-contained true -p:PublishTrimmed=True -p:TrimMode=Link -c release -o out --no-restore


# https://mcr.microsoft.com/v2/dotnet/runtime-deps/tags/list
FROM mcr.microsoft.com/dotnet/runtime-deps:5.0.6-alpine3.13-amd64
WORKDIR /app
COPY --from=publish /app/src/out ./
EXPOSE 7070
ENV COMPlus_EnableDiagnostics=0
USER 1000
ENTRYPOINT ["/app/pubsubpublisher"]