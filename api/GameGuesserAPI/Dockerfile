# Use Microsoft official .NET SDK image to build the app
FROM mcr.microsoft.com/dotnet/sdk:8.0 AS build
WORKDIR /src

# Copy everything and restore dependencies
COPY . .
RUN dotnet restore

# Build the project in Release mode
RUN dotnet publish -c Release -o /app

# Use a smaller runtime image
FROM mcr.microsoft.com/dotnet/aspnet:8.0 AS runtime
WORKDIR /app
COPY --from=build /app .

# Tell .NET to bind to any host (Render injects a $PORT variable)
ENV ASPNETCORE_URLS=http://+:$PORT

# Run the app
ENTRYPOINT ["dotnet", "GameGuesserAPI.dll"]
