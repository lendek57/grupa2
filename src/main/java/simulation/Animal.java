package simulation;

public class Animal {
	private Vector2D position;

	public Animal(Vector2D position) {
		this.position = position;
	}

	public Vector2D getPosition() {
		return position;
	}

	public void move(MapDirection direction) {
		position = position.add(direction.getUnitVector());
		System.out.println("Animal moves " + direction + ": new position: " + position);
	}
}
