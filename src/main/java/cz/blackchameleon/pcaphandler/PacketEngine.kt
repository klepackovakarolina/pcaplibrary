package cz.blackchameleon.pcaphandler

import timber.log.*
import java.nio.*

/**
 * PacketEngine
 * Engine for packet handling
 *
 * @author Karolina Klepackova <klepackova.karolina@email.cz>
 * @since ver 1.0
 */

class PacketEngine {
  private val TAG = PacketEngine::class.java.name

  fun createPacketHeader(length: Int): ByteArray {
    Timber.d(TAG, "createPacketHeader(): %d", length)
    /* Struct got from https://wiki.wireshark.org/Development/LibpcapFileFormat
    typedef struct pcaprec_hdr_s {
        guint32 ts_sec;         /* timestamp seconds */  13 ac fd 50  (0001001110101100 1111110101010000)2_b
        guint32 ts_usec;        /* timestamp microseconds */  88 f3 0b 00  (1000100011110011 0000101100000000)2_b
        guint32 incl_len;       /* number of octets of packet saved in file */  34 00 00 00  (0011010000000000 0000000000000000)2_b
        guint32 orig_len;       /* actual length of packet */  34 00 00 00  (0011010000000000 0000000000000000)2_b
        } pcaprec_hdr_t;
    */
    val byteBuffer = ByteBuffer.allocate(16)
    byteBuffer.order(ByteOrder.LITTLE_ENDIAN)

    // 0xFB 0x15 0xF5 0x55 seconds
    byteBuffer.put(0xFB.toByte())
    byteBuffer.put(0x15.toByte())
    byteBuffer.put(0xF5.toByte())
    byteBuffer.put(0x55.toByte())
    // 0x98 0x6A 0x0B 0x00 microseconds
    byteBuffer.put(0x98.toByte())
    byteBuffer.put(0x6A.toByte())
    byteBuffer.put(0x0B.toByte())
    byteBuffer.put(0x00.toByte())
    //incl_len;orig_len
    // + 14 for the L2 header size
    byteBuffer.putInt(length + 14)
    byteBuffer.putInt(length + 14)

    return byteBuffer.array()
  }

  fun createL2Header(): ByteArray {
    Timber.d(TAG, "createL2Header()")
    val byteBuffer = ByteBuffer.allocate(14)
    // destination addr
    byteBuffer.put(0x00.toByte())
    byteBuffer.put(0xE0.toByte())
    byteBuffer.put(0x81.toByte())
    byteBuffer.put(0xD7.toByte())
    byteBuffer.put(0xB5.toByte())
    byteBuffer.put(0xA6.toByte())
    // source addr
    byteBuffer.put(0xE0.toByte())
    byteBuffer.put(0xAC.toByte())
    byteBuffer.put(0xF1.toByte())
    byteBuffer.put(0x74.toByte())
    byteBuffer.put(0x73.toByte())
    byteBuffer.put(0xC3.toByte())
    // IPv4
    byteBuffer.put(0x08.toByte())
    byteBuffer.put(0x00.toByte())

    return byteBuffer.array()
  }

  fun createGlobalHeader(): ByteArray {
    Timber.d(TAG, "createGlobalHeader()")
    /* Struct got from https://wiki.wireshark.org/Development/LibpcapFileFormat
    typedef struct pcap_hdr_s {
    guint32 magic_number;   /* magic number */  d4 c3 b2 a1    little-endian   (1101010011000011   1011001010100001)2_b
    guint16 version_major;  /* major version number */ 02 00   (0000001000000000) 2_b
    guint16 version_minor;  /* minor version number */ 04 00   (0000010000000000) 2_b
    gint32  thiszone;       /* GMT to local correction */ 00 00 00 00  (0000000000000000 0000000000000000)2_b
    guint32 sigfigs;        /* accuracy of timestamps */ 00 00 00 00  (0000000000000000 0000000000000000)2_b
    guint32 snaplen;        /* max length of captured packets, in octets */  ff ff 00 00 (1111111111111111 0000000000000000)2_b
    guint32 network;        /* data link type */  01 00 00 00  (0000000100000000 0000000000000000)2_b
    } pcap_hdr_t;
     */
    val byteBuffer = ByteBuffer.allocate(24)
    //magic_number
    byteBuffer.put(0xd4.toByte())
    byteBuffer.put(0xc3.toByte())
    byteBuffer.put(0xb2.toByte())
    byteBuffer.put(0xa1.toByte())
    // version_major;version_minor
    byteBuffer.put(0x02.toByte())
    byteBuffer.put(0x00.toByte())
    byteBuffer.put(0x04.toByte())
    byteBuffer.put(0x00.toByte())
    //thiszone
    byteBuffer.put(0x00.toByte())
    byteBuffer.put(0x00.toByte())
    byteBuffer.put(0x00.toByte())
    byteBuffer.put(0x00.toByte())
    //sigfigs
    byteBuffer.put(0x00.toByte())
    byteBuffer.put(0x00.toByte())
    byteBuffer.put(0x00.toByte())
    byteBuffer.put(0x00.toByte())
    // snaplen
    byteBuffer.put(0xff.toByte())
    byteBuffer.put(0xff.toByte())
    byteBuffer.put(0x00.toByte())
    byteBuffer.put(0x00.toByte())
    // network
    byteBuffer.put(0x01.toByte())
    byteBuffer.put(0x00.toByte())
    byteBuffer.put(0x00.toByte())
    byteBuffer.put(0x00.toByte())

    return byteBuffer.array()
  }

}