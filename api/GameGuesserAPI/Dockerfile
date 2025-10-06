# In api/GameGuesserAPI/Dockerfile
FROM mcr.microsoft.com/dotnet/sdk:9.0 AS build
WORKDIR /src

# Copy the csproj and restore dependencies
COPY GameGuesserAPI.csproj ./
RUN dotnet restore

# Copy everything else (all source files)
COPY . ./

# Publish the project
RUN dotnet publish -c Release -o /app

# Runtime image
FROM mcr.microsoft.com/dotnet/aspnet:9.0
WORKDIR /app
COPY --from=build /app ./

EXPOSE 5000
ENTRYPOINT ["dotnet", "GameGuesserAPI.dll"]

