using GameGuesserAPI.Models;
using Microsoft.AspNetCore.Mvc;

namespace GameGuesserAPI.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class GamesController : Controller
    {
        private static List<Game> Games = new List<Game>
        {
            new Game
            {
                Id = 1,
                Name = "The Legend of Zelda: Breath of the Wild",
                Genre = "Action-adventure",
                Platforms = new List<string> {"Nintendo Switch" },
                ReleaseYear = 2017,
                Developer = "Nintendo",
                Publisher = "Nintendo",
                Description = "An open-world adventure game where players explore the vast kingdom of Hyrule.",
                CoverImageUrl = "https://www.nintendo.com/eu/media/images/10_share_images/games_15/wiiu_14/SI_WiiU_TheLegendOfZeldaBreathOfTheWild_image1600w.jpg",
                Budget = 100000000,
                Saga = "The Legend of Zelda",
                POV = "Third-person",
                Keywords = new List<string> { "open-world", "exploration", "puzzles", "adventure" },
                Clues = new List<string> { "Hyrule", "Master Sword", "Link", "Princess Zelda" }
            },
            new Game
            {
                Id = 2,
                Name = "God of War(2018)",
                Genre = "Action-adventure",
                Platforms = new List<string> {"PlayStation 4", "PlayStation 5", "PC" },
                ReleaseYear = 2018,
                Developer = "Santa Monica Studio",
                Publisher = "Sony Interactive Entertainment",
                Description = "A mythological adventure following Kratos and his son Atreus in the world of Norse gods.",
                CoverImageUrl = "https://gaming-cdn.com/images/products/7325/616x353/god-of-war-pc-game-steam-europe-cover.jpg?v=1744715989",
                Budget = 50000000,
                Saga = "God of War",
                POV = "Third-person",
                Keywords = new List<string> { "mythology", "father-son", "combat", "adventure" },
                Clues = new List<string> { "Kratos", "Atreus", "Norse gods", "Leviathan Axe" }
            },
            new Game
            {
                Id = 3,
                Name = "The Witcher 3: Wild Hunt",
                Genre = "Action RPG",
                Platforms = new List<string> {"PC", "PlayStation 4", "Xbox One", "Nintendo Switch" },
                ReleaseYear = 2015,
                Developer = "CD Projekt Red",
                Publisher = "CD Projekt",
                Description = "An open-world RPG where players control Geralt of Rivia, a monster hunter, in a richly detailed fantasy world.",
                CoverImageUrl = "https://store-images.s-microsoft.com/image/apps.46303.69531514236615003.534d4f71-03cb-4592-929a-b00a7de28b58.abbf704e-3676-4fb7-9f68-f4425de5df24?q=90&w=480&h=270",
                Budget = 81000000,
                Saga = "The Witcher",
                POV = "Third-person",
                Keywords = new List<string> { "RPG", "open-world", "fantasy", "monsters" },
                Clues = new List<string> { "Geralt of Rivia", "Ciri", "Yennefer", "Wild Hunt" }
            },
            new Game
            {
                Id = 4,
                Name = "Red Dead Redemption 2",
                Genre = "Action-adventure",
                Platforms = new List<string> {"PlayStation 4", "Xbox One", "PC" },
                ReleaseYear = 2018,
                Developer = "Rockstar Games",
                Publisher = "Rockstar Games",
                Description = "An epic tale of life in America at the dawn of the modern age, following outlaw Arthur Morgan.",
                CoverImageUrl = "https://store-images.s-microsoft.com/image/apps.58752.68182501197884443.ac728a87-7bc1-4a0d-8bc6-0712072da93c.0cf58754-9802-46f8-8557-8d3ff32a627a?q=90&w=480&h=270",
                Budget = 370000000,
                Saga = "Red Dead",
                POV = "Third-person",
                Keywords = new List<string> { "wild west", "open-world", "story-driven", "action" },
                Clues = new List<string> { "Arthur Morgan", "Dutch van der Linde", "outlaw", "gang" }
            },
            new Game
            {
                Id = 5,
                Name = "Minecraft",
                Genre = "Sandbox, Survival",
                Platforms = new List<string> {"PC", "PlayStation", "Xbox", "Nintendo Switch", "Mobile" },
                ReleaseYear = 2011,
                Developer = "Mojang Studios",
                Publisher = "Mojang Studios",
                Description = "A sandbox game that allows players to build and explore infinite worlds made of blocks.",
                CoverImageUrl = "https://www.nintendo.com/eu/media/images/10_share_images/games_15/nintendo_switch_4/2x1_NSwitch_Minecraft.jpg",
                Budget = 0,
                Saga = "Minecraft",
                POV = "First-person, Third-person",
                Keywords = new List<string> { "sandbox", "building", "survival", "creativity" },
                Clues = new List<string> { "blocks", "Creeper", "Steve", "Enderman" }
            },
            new Game
            {
                Id = 6,
                Name = "Hades",
                Genre = "Roguelike, Action",
                Platforms = new List<string> {"PC", "Nintendo Switch", "PlayStation 4", "PlayStation 5", "Xbox One", "Xbox Series X/S" },
                ReleaseYear = 2020,
                Developer = "Supergiant Games",
                Publisher = "Supergiant Games",
                Description = "A roguelike dungeon crawler where players control Zagreus, the son of Hades, as he attempts to escape the Underworld.",
                CoverImageUrl = "https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/1145360/header.jpg?t=1715722799",
                Budget = 2000000,
                Saga = "Hades",
                POV = "Top-down",
                Keywords = new List<string> { "roguelike", "dungeon crawler", "mythology", "action" },
                Clues = new List<string> { "Zagreus", "Hades", "Underworld", "Greek gods" }
            }
        };

        [HttpGet("random")]
        public IActionResult GetRandomGame()
        {
            var random = new Random();
            var game = Games[random.Next(Games.Count)];
            return Ok(new { game.Id, game.Keywords });
        }

        private static Dictionary<int, int> GameClueIndex = new Dictionary<int, int>();

        [HttpPost("guess")]
        public IActionResult SubmitGuess(int gameId, string guess)
        {
            var game = Games.FirstOrDefault(g => g.Id == gameId);
            if (game == null)
                return NotFound("Game not found.");

            if (guess.Equals(game.Name, StringComparison.OrdinalIgnoreCase))
            {
                if (GameClueIndex.ContainsKey(gameId))
                    GameClueIndex[gameId] = 0;

                return Ok(new { correct = true, message = "Correct guess!" });
            }
            int clueIndex = 0;
            if (GameClueIndex.ContainsKey(gameId))
            {
                clueIndex = GameClueIndex[gameId];
            }

            string clue = clueIndex < game.Clues.Count ? game.Clues[clueIndex] : "No more clues available.";

            GameClueIndex[gameId] = clueIndex + 1;

            return Ok(new { correct = false, hint = clue });
        }
    }
}