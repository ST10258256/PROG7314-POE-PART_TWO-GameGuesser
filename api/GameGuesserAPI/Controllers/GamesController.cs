using Microsoft.AspNetCore.Mvc;
using GameGuesserAPI.Models;
using GameGuesserAPI.Services;

[ApiController]
[Route("api/[controller]")]
public class GamesController : ControllerBase
{
    private readonly GameService _gameService;
    private static Dictionary<int, int> GameClueIndex = new Dictionary<int, int>();

    public GamesController(GameService gameService)
    {
        _gameService = gameService;
    }

    [HttpGet("random")]
    public async Task<IActionResult> GetRandomGame()
    {
        var game = await _gameService.GetRandomGameAsync();
        return Ok(new
        {
            game.Id,
            game.Name,
            game.CoverImageUrl,
            game.Keywords
        });
    }

    [HttpPost("guess")]
    public async Task<IActionResult> SubmitGuess(int gameId, string guess)
    {
        var game = await _gameService.GetGameByIdAsync(gameId);
        if (game == null) return NotFound("Game not found.");

        if (guess.Equals(game.Name, StringComparison.OrdinalIgnoreCase))
        {
            if (GameClueIndex.ContainsKey(gameId))
                GameClueIndex[gameId] = 0;

            return Ok(new { correct = true, message = "Correct guess!" });
        }

        int clueIndex = GameClueIndex.ContainsKey(gameId) ? GameClueIndex[gameId] : 0;
        string clue = clueIndex < game.Clues.Count ? game.Clues[clueIndex] : "No more clues available.";
        GameClueIndex[gameId] = clueIndex + 1;

        return Ok(new { correct = false, hint = clue });
    }

    [HttpGet]
    public async Task<IActionResult> GetAllGames()
    {
        var gameNames = await _gameService.GetAllGameNamesAsync();
        return Ok(gameNames);
    }

    [HttpGet("{id}")]
    public async Task<IActionResult> GetGameById(int id)
    {
        var game = await _gameService.GetGameByIdAsync(id);
        if (game == null) return NotFound();
        return Ok(game);
    }

    [HttpGet("full")]
    public async Task<IActionResult> GetAllGamesFull()
    {
        try
        {
            var games = await _gameService.GetAllGamesAsync();
            return Ok(games);
        }
        catch (Exception ex)
        {
            return StatusCode(500, ex.Message);
        }
    }


}
