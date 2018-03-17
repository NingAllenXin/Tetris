package tetris;

import java.util.Random;

public class Tetromino extends Cell {

	protected Cell[] cells = new Cell[4];

	public void moveDown() {
		for (int i = 0; i < cells.length; i++) {
			cells[i].moveDown();
		}
	}

	public void moveLeft() {
		for (int i = 0; i < cells.length; i++) {
			cells[i].moveLeft();
		}
	}

	public void moveRight() {
		for (int i = 0; i < cells.length; i++) {
			cells[i].moveRight();
		}
	}
	
	public int[] inside() {
		int[] inside = new int[]{1000, 0, 1000, 0};
		for (int i = 0; i < cells.length; i++) {
			inside[0] = Math.min(inside[0], cells[i].getRow());
			inside[1] = Math.max(inside[1], cells[i].getRow());
			inside[2] = Math.min(inside[2], cells[i].getCol());
			inside[3] = Math.max(inside[3], cells[i].getCol());
		}
		return inside;
	}

	public static Tetromino ranShape() {
		Random random = new Random();
		int index = random.nextInt(7);
		switch (index) {
		case 0:
			return new J();
		case 1:
			return new L();
		case 2:
			return new O();
		case 3:
			return new Z();
		case 4:
			return new S();
		case 5:
			return new I();
		case 6:
			return new T();
		}
		return null;
	}

	public Cell[] spin() {
		if (this.getClass().equals(new O().getClass()))
			return null;
		Cell[] iCells = new Cell[4];
		int iRow = this.cells[2].getRow();
		int iCol = this.cells[2].getCol();
		for (int i = 0; i < this.cells.length; i++) {
			int nRow = this.cells[i].getRow();
			int nCol = this.cells[i].getCol();
			iCells[i] = new Cell(iRow - iCol + nCol, iRow + iCol - nRow,
					this.cells[i].getBgImage());
		}
		return iCells;
	}

	public Cell[] spin1() {
		if (this.getClass().equals(new O().getClass()))
			return null;
		Cell[] iCells = new Cell[4];
		int iRow = this.cells[2].getRow();
		int iCol = this.cells[2].getCol();
		for (int i = 0; i < this.cells.length; i++) {
			int nRow = this.cells[i].getRow();
			int nCol = this.cells[i].getCol();
			iCells[i] = new Cell(iRow + iCol - nCol, iCol - iRow + nRow,
					this.cells[i].getBgImage());
		}
		return iCells;
	}

}

class J extends Tetromino {
	public J() {
		cells[0] = new Cell(2, 5, Tetris.J);
		cells[1] = new Cell(0, 6, Tetris.J);
		cells[2] = new Cell(2, 6, Tetris.J);
		cells[3] = new Cell(1, 6, Tetris.J);
	}
}

class L extends Tetromino {

	public L() {
		cells[0] = new Cell(2, 6, Tetris.L);
		cells[1] = new Cell(0, 5, Tetris.L);
		cells[2] = new Cell(2, 5, Tetris.L);
		cells[3] = new Cell(1, 5, Tetris.L);
	}
}

class O extends Tetromino {
	public O() {
		cells[0] = new Cell(0, 5, Tetris.O);
		cells[1] = new Cell(0, 6, Tetris.O);
		cells[2] = new Cell(1, 5, Tetris.O);
		cells[3] = new Cell(1, 6, Tetris.O);
	}
}

class Z extends Tetromino {
	public Z() {
		cells[0] = new Cell(0, 4, Tetris.Z);
		cells[1] = new Cell(0, 5, Tetris.Z);
		cells[2] = new Cell(1, 5, Tetris.Z);
		cells[3] = new Cell(1, 6, Tetris.Z);
	}
}

class S extends Tetromino {
	public S() {
		cells[0] = new Cell(1, 4, Tetris.S);
		cells[1] = new Cell(1, 5, Tetris.S);
		cells[2] = new Cell(0, 5, Tetris.S);
		cells[3] = new Cell(0, 6, Tetris.S);
	}
}

class I extends Tetromino {
	public I() {
		cells[0] = new Cell(0, 5, Tetris.I);
		cells[1] = new Cell(1, 5, Tetris.I);
		cells[2] = new Cell(2, 5, Tetris.I);
		cells[3] = new Cell(3, 5, Tetris.I);
	}
}

class T extends Tetromino {
	public T() {
		cells[0] = new Cell(0, 4, Tetris.T);
		cells[1] = new Cell(0, 6, Tetris.T);
		cells[2] = new Cell(0, 5, Tetris.T);
		cells[3] = new Cell(1, 5, Tetris.T);
	}
}