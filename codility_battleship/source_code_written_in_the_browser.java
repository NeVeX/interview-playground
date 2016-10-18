

// you can also use imports, for example:
import java.util.*;

// you can write to stdout for debugging purposes, e.g.
// System.out.println("this is a debug message");

class Solution {
    
    private final static String NO_SHIPS_SUNK_OR_HIT = "0,0";
    
    // Note, the input Strings are not modified (e.g. UpperCased) - the format is expected to be consistent
    // I.E: 0 > N <= 26, S is UPPER Case and so is T
    public String solution(int N, String S, String T) {
        // create the coordinates of the ships
        Set<ShipCoordinates> inputShipCoordinates = getShipLocationsFromInput(S);
        if ( inputShipCoordinates.size() == 0 ) { 
            return NO_SHIPS_SUNK_OR_HIT; // Could maybe return error codes instead? Told the input would be valid though
        }
        // create the tiles of the targets
        Set<Tile> inputTargets = getTargetsFromInput(T);
        if ( inputTargets.size() == 0 ) {
            return NO_SHIPS_SUNK_OR_HIT; // Again, maybe an error code is more appropriate than a "result"
        }
        
        // Create the list of ships to use
        List<Ship> ships = new ArrayList<Ship>();
        for ( ShipCoordinates inputShipCoordinate : inputShipCoordinates ) {
            ships.add(new Ship(inputShipCoordinate));   
        }
        
        // If we get here, the ships are targets are known, so now create a board of tiles
        Board board = new Board(N);
        // Add all the ships to the board 
        board.addShips(ships);
        
        // We have our board, the ships are on the board, so now, let's effectively play the game of hitting the ship
        for ( Tile target : inputTargets) {
            board.applyTargetHit(target);   
        }
        
        // All targets have being "applied" to the board, now we just need to ask the board for the status
        int totalShipsHitButNotSunken = board.getTotalShipsHitButNotSunk();
        int totalShipsSunken = board.getTotalShipsSunk();
        
        return totalShipsSunken + "," + totalShipsHitButNotSunken;
    }
    
    private Set<Tile> getTargetsFromInput(String inputTargets) {
        // We expect the targets in the form "1A 3E 5T 6Y" - space delimited
        Set<Tile> validTargets = new HashSet<>();
        String[] inputTargetsSplit = inputTargets.split(" ");
        for (String inputTarget : inputTargetsSplit) {
            String validTarget = inputTarget.trim();
            if ( validTarget.length() >= 2 ) { // "2A" or "26A" => make sure we have at least these sizes
                Tile target = new Tile(validTarget);
                validTargets.add(target);
            }
        }
        return validTargets;
    }
    
    private Set<ShipCoordinates> getShipLocationsFromInput(String inputShipString) {
        // Expect the form "1A 4C, 2G 2G, 4Q 6Q"
        Set<ShipCoordinates> validCoordinates = new HashSet<>();
        String[] shipsSplitAtComma = inputShipString.split(",");
        for ( String shipLocations : shipsSplitAtComma) {
            // we have say something like "1A 4C" or "4E 4E"
            // Make sure we have a space in between
            String validShipLocations = shipLocations.trim(); // get rid of outside spaces
            if ( validShipLocations.length() >= 5 ) { // Make sure empty strings and such don't get through (5 = "1X 2Y" => at least 5 chars)
                String[] shipLocationsSplit = validShipLocations.split(" ");
                if ( shipLocationsSplit.length == 2 ) {
                    // concludes our validation, so add the new Ship
                    validCoordinates.add(new ShipCoordinates(shipLocationsSplit[0], shipLocationsSplit[1]));
                }
            }
        }
        return validCoordinates;
    }
    
    
    private static class ShipCoordinates {
        private final String topLeftLocation;
        private final String bottomRightLocation;
        
        ShipCoordinates(String inputTopLeft, String inputBottomRight) {
            this.topLeftLocation = inputTopLeft;
            this.bottomRightLocation = inputBottomRight;
        }
        
        @Override // To remove duplicates in the Set, we need to override this method with the correct impl
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ShipCoordinates that = (ShipCoordinates) o;
            return Objects.equals(topLeftLocation, that.topLeftLocation) &&
                    Objects.equals(bottomRightLocation, that.bottomRightLocation);
        }

        @Override // To remove duplicates in the Set, we need to override this method with the correct impl
        public int hashCode() {
            return Objects.hash(topLeftLocation, bottomRightLocation);
        }
    }
    
    // Class the represents everything Ship related - the tiles it consumes and the hit targets
    private static class Ship {
        private final Set<Tile> shipTiles = new HashSet<>();
        private Set<Tile> shipHitTiles = new HashSet<>(); // Store the tiles that are hit
        private final ShipCoordinates coordinates;
        
        Ship(ShipCoordinates coordinates) {
            this.coordinates = coordinates;
            createShipTiles();
        }
        
        private void createShipTiles() {
            // We need to create a "map" of this ship (all the co-ordinates in between these points
            // The first letter (and second if applicable) is the row number, the second letter is the column (expected as upper case)
            // So get the row, loop over all letters and add to the set of tiles
            int startRow = getRowNumberFromCoordinate(coordinates.topLeftLocation);
            char startColumn = getColumnCharFromCoordinate(coordinates.topLeftLocation);
            int endRow = getRowNumberFromCoordinate(coordinates.bottomRightLocation);
            char endColumn = getColumnCharFromCoordinate(coordinates.bottomRightLocation);
            
            for ( int row = startRow; row <= endRow; row++ ) {
                // Loop over all the chars in the range (since at most this is 26 * 26 - assume this performance is ok (from test notes)
                for ( char alphaChar = startColumn; alphaChar <= endColumn; alphaChar++) { // In java, we can loop over the chars
                    // We have the row and column, so add it to the tile set
                    Tile partOfShipTile = new Tile(row + "" + alphaChar);
                    // System.out.println("Ship: "+(row +""+alphaChar));
                    shipTiles.add(partOfShipTile); // add this to the ship "map" of it's tiles
                }
            }
        }   
        
        // Given a string "2A" or "23B" this would return 2 and 23
        private int getRowNumberFromCoordinate(String coordinate) {
            if ( coordinate.length() > 2 ) {
                return Integer.valueOf(coordinate.substring(0, 2));
            }
            return Integer.valueOf(coordinate.substring(0, 1));
        }
        
        private char getColumnCharFromCoordinate(String coordinate) {
            return coordinate.charAt(coordinate.length()-1); // Get the last character
        }
        
        // This ship has being hit - register the hit
        void applyHit(Tile target) {
            // Just make sure we have this target
            if ( shipTiles.contains(target) ) {
                // yup we are hit, so add it to the set of hit tiles (note this will overwrite other tiles, which is fine - 1 hit or more is the same as 2 or more
                shipHitTiles.add(target);
            }
        }
        
        // Returns True is this ship has all it's tiles hit - that it is now sunk
        boolean isSunken() {
            return shipHitTiles.size() == shipTiles.size();
        }
        
        boolean isAlreadyHit() {
            return shipHitTiles.size() > 0;
        }
    }
    
    // Class that represents the Board on which ships will be laid out 
    private static class Board {
        private final int size; // board is always square (size * size)
        private int totalShipsHitButNotSunk = 0;
        private int totalShipsSunken = 0;
        private Map<Tile, Ship> tileToShip = new HashMap<>(); 
        
        Board(int inputSize) {
            this.size = inputSize;   
            // Draw the board, loop over the size and the characters (A-Z) - start index at 1 for Set lookup (hash)
            for ( int row = 1; row <= size; row++) {
                for (int alphaChar = 1; alphaChar <= size; alphaChar++) {
                    char upperCaseChar = (char) (alphaChar + 64); // Get the UPPER case char
                    String tileLocation = row + "" + upperCaseChar; // 1A, 1B, 1C...2A, 2B, 2C...etc
                    tileToShip.put(new Tile(tileLocation), null); // Create the tile - but don't map it to any ship (not yet anyhow)
                }
            }
        }
        
        // Add the ships onto the board
        void addShips(List<Ship> ships) {
            // Since we are told no ships will overlap, we can assume no collisions
            for ( Ship ship : ships ) {
                Set<Tile> shipTiles = ship.shipTiles;
                for ( Tile shipTile : shipTiles ) {
                    tileToShip.put(shipTile, ship); // Store that this tile maps to a particular ship   
                }
            }
        }
        
        // Given a target, this will set the taget somewhere on the board (hitting ships possibly)
        void applyTargetHit(Tile target) {
            // Since we only care about ship hits, we'll only try to get ships on the board (empty tiles are meaningless here)
            // So, get the ship that this matches this tile
            Ship shipToHit = tileToShip.get(target);
            if ( shipToHit != null) { // Ship can be null - since not all places have ships
                boolean hitBefore = shipToHit.isAlreadyHit();
                if ( !hitBefore ) {
                    // First hit, so add it to the counter of ships hit (this hit could sink the ship though, we check this later)
                    totalShipsHitButNotSunk++; 
                }
                // So apply the hit to the ship
                shipToHit.applyHit(target);
                
                if ( shipToHit.isSunken() ) {
                    // Welp. this ship is now sunken! So add it to the counter
                    totalShipsSunken++;
                    // Deduct from the ships that are hit also
                    totalShipsHitButNotSunk--;
                }
            }
        }
        
        int getTotalShipsHitButNotSunk() {
            return totalShipsHitButNotSunk;   
        }
        
        int getTotalShipsSunk() {
            return totalShipsSunken;   
        }
        
    }
    
    // Class that represents tiles on the board, tiles of a ship and targets
    private static class Tile {
        private final String location;
        
        Tile(String inputLocation) {
            this.location = inputLocation;   
        }
        
        @Override // need to override these for the set lookups - note method generated from IDE
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Tile tile = (Tile) o;
            return Objects.equals(location, tile.location);
        }

        @Override // need to override these for the set lookups - note method generated from IDE
        public int hashCode() {
            return Objects.hash(location);
        }
    }
}