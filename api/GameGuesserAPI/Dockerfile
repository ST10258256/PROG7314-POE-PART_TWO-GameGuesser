# Use .NET 9 SDK (Preview) for building the project
FROM mcr.microsoft.com/dotnet/sdk:9.0 AS build
WORKDIR /src

# Copy project files
COPY api/GameGuesserAPI/*.csproj ./GameGuesserAPI/
RUN dotnet restore ./GameGuesserAPI/GameGuesserAPI.csproj

# Copy the rest of the source
COPY api/GameGuesserAPI/. ./GameGuesserAPI/
WORKDIR /src/GameGuesserAPI

# Build and publish
RUN dotnet publish -c Release -o /app

# Use .NET 9 runtime (Preview) for running the app
FROM mcr.microsoft.com/dotnet/aspnet:9.0 AS runtime
WORKDIR /app
COPY --from=build /app ./

# Expose port (Render usually maps automatically, but 8080 is safe)
EXPOSE 8080

ENTRYPOINT ["dotnet", "GameGuesserAPI.dll"]
