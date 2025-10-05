using MongoDB.Driver;
using GameGuesserAPI.Models;

namespace GameGuesserAPI.Services
{
    public class GameService
    {
    private readonly IMongoCollection<Game> _games;

    public GameService(IMongoClient mongoClient)
    {
        var database = mongoClient.GetDatabase("GameGuesserDB");
        _games = database.GetCollection<Game>("games");
    }

    public async Task<List<Game>> GetAllGamesAsync() =>
        await _games.Find(_ => true).ToListAsync();

    public async Task<Game> GetGameByIdAsync(int id) =>
        await _games.Find(g => g.Id == id).FirstOrDefaultAsync();

    public async Task<Game> GetRandomGameAsync()
    {
        var allGames = await GetAllGamesAsync();
        var random = new Random();
        return allGames[random.Next(allGames.Count)];
    }

    public async Task InsertGameAsync(Game game) =>
        await _games.ReplaceOneAsync(
            g => g.Id == game.Id, game,
            new ReplaceOptions { IsUpsert = true });

    public async Task<List<string>> GetAllGameNamesAsync() =>
        await _games.Find(_ => true)
                    .Project(g => g.Name)
                    .ToListAsync();
}
  
}


