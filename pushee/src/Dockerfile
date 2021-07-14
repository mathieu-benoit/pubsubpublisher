# https://mcr.microsoft.com/v2/dotnet/sdk/tags/list
FROM mcr.microsoft.com/dotnet/sdk:5.0.302 AS build
WORKDIR /app
COPY pushee.csproj ./
WORKDIR /app
RUN dotnet restore pushee.csproj -r linux-musl-x64
COPY / .

FROM build AS publish
WORKDIR /app
RUN dotnet publish pushee.csproj -r linux-musl-x64 --self-contained true -p:PublishTrimmed=True -p:TrimMode=Link -c release -o out --no-restore

# https://mcr.microsoft.com/v2/dotnet/runtime-deps/tags/list
FROM mcr.microsoft.com/dotnet/runtime-deps:5.0.8-alpine3.13-amd64
WORKDIR /app
COPY --from=publish /app/out ./
EXPOSE 7070
ENV COMPlus_EnableDiagnostics=0
USER 1000
ENTRYPOINT ["/app/pushee"]