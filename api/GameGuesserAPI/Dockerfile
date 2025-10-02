# --- Build stage ---
FROM mcr.microsoft.com/dotnet/sdk:9.0 AS build
WORKDIR /src

# Copy the csproj first and restore dependencies
COPY api/GameGuesserAPI/GameGuesserAPI.csproj ./GameGuesserAPI/
RUN dotnet restore ./GameGuesserAPI/GameGuesserAPI.csproj

# Copy all source files
COPY api/GameGuesserAPI/. ./GameGuesserAPI/
WORKDIR /src/GameGuesserAPI

# Publish the app
RUN dotnet publish -c Release -o /app

# --- Runtime stage ---
FROM mcr.microsoft.com/dotnet/aspnet:9.0
WORKDIR /app
COPY --from=build /app ./

# Expose the port your API uses (adjust if different)
EXPOSE 5000

ENTRYPOINT ["dotnet", "GameGuesserAPI.dll"]
