
#ifndef __piecegenerator__
#define __piecegenerator__

class PieceGenerator
{
	public:
		PieceGenerator() {};
		virtual ~PieceGenerator() {};
		virtual int getPiece() =0;
};

#endif
