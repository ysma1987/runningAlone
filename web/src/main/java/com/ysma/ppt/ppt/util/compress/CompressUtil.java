package com.ysma.ppt.ppt.util.compress;

import java.io.IOException;

public enum CompressUtil {
	DEFLATER {
		ICompress compress = new DeflateCompress();

		public byte[] compress(byte[] data) throws IOException {
			return compress.compress(data);
		}

		public byte[] uncompress(byte[] data) throws IOException {
			return compress.uncompress(data);
		}
	},
	BZIP2 {
		ICompress compress = new BzipCompress();

		public byte[] compress(byte[] data) throws IOException {
			return compress.compress(data);
		}

		public byte[] uncompress(byte[] data) throws IOException {
			return compress.uncompress(data);
		}
	},
	GZIP {
		ICompress compress = new GzipCompress();

		public byte[] compress(byte[] data) throws IOException {
			return compress.compress(data);
		}

		public byte[] uncompress(byte[] data) throws IOException {
			return compress.uncompress(data);
		}
	},
	LZ4 {
		ICompress compress = new Lz4Compress();

		public byte[] compress(byte[] data) throws IOException {
			return compress.compress(data);
		}

		public byte[] uncompress(byte[] data) throws IOException {
			return compress.uncompress(data);
		}
	},
	LZO {
		ICompress compress = new LzoCompress();

		public byte[] compress(byte[] data) throws IOException {
			return compress.compress(data);
		}

		public byte[] uncompress(byte[] data) throws IOException {
			return compress.uncompress(data);
		}
	},
	SNAPPY {
		ICompress compress = new SnappyCompress();

		public byte[] compress(byte[] data) throws IOException {
			return compress.compress(data);
		}

		public byte[] uncompress(byte[] data) throws IOException {
			return compress.uncompress(data);
		}
	};

	public byte[] compress(byte[] data) throws IOException {
		throw new AbstractMethodError();
	}

	public byte[] uncompress(byte[] data) throws IOException {
		throw new AbstractMethodError();
	}
}