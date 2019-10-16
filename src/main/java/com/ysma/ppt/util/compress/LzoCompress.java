package com.ysma.ppt.util.compress;

/**
 * lzo压缩:LZO是致力于解压速度的一种数据压缩算法，
 * LZO是致力于解压速度的一种数据压缩算法，LZO 是 Lempel-Ziv-Oberhumer 的缩写。
 * 这个算法是无损算法，参考实现程序是线程安全的。 实现它的一个自由软件工具是lzop。现在 LZO 有用于 Perl、Python 以及 Java 的各种版本
 * https://blog.csdn.net/ihaveadream511/article/details/73822052
 *
 * LZO压缩算法采用(重复长度L，指回距离D)代替当前已经在历史字符串中出现过的字符串，
 * 其中，重复长度是指，后出现的字符串与先出现的字符串中连续相同部分的长度;指回距离是指，
 * 先后两个相同字符串之间相隔的距离(每个字节为一个单位);如果没出现过(定义为新字符)，
 * 则首先输出新字符的个数，再输出新字符。例如，待处理的字符串为“ABCDEFGHABCDEFJKLM”，
 * 压缩算法逐个处理字符，处理ABCDEFGH时没发现重复字符;处理到ABCDEF时发现这些字符在历史字符串中已经出现过，
 * 计算重复长度为6，指回距离(当前A离历史A的距离)为8，则用(6,8)代替ABCDEF;处理到JKLM时没发现重复字符，字符串到此处理完毕，
 * 则整个字符串被压缩成：(08)h ABCDEFGH(6,8)(04)h JKLM，其中h表示16进制
 */
public class LzoCompress extends AbstCompress implements ICompress {
    /**
     * LZO压缩特点：
     * 解压速度很快，并且很简单；
     * 解压时不需要内存支持；
     * 压缩的速度还不错；
     * 压缩时只需要 64 KiB 的内存支持；
     * 压缩比例可以根据需要调节，而这并不影响解压的效率，提高压缩比例自然会降低压缩速度；
     * 压缩包含了很多的压缩级别，提供很多选择；
     * 提供只需要 8 KiB 内存支持的压缩级别；
     * 提供线程安全；
     * 提供无损压缩
     */
    @Override
    public byte[] compress(byte[] data) {

        return new byte[0];
    }

    @Override
    public byte[] uncompress(byte[] data) {
        return new byte[0];
    }
}
