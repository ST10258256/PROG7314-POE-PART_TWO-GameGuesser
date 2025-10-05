using System.Globalization;

namespace GameGuesserAPI.Models
{
    public class Game
    {
        [BsonId] 
        [BsonRepresentation(BsonType.ObjectId)]
        public string Id { get; set; } 
        public string Name { get; set; }
        public string Genre { get; set; }
        public List<string> Platforms { get; set; }
        public int ReleaseYear { get; set; }
        public string Developer { get; set; }
        public string Publisher { get; set; }
        public string Description { get; set; }
        public string CoverImageUrl { get; set; }
        public Double Budget { get; set; }
        public string Saga { get; set; }
        public string POV { get; set; }
        public List<string> Keywords { get; set; }
        public List<string> Clues { get; set; }
    }
}