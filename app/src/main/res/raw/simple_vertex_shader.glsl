
// vec4是一个包含4个分量的向量，在位置的上下文中，可以认为这4个分量是x,y,z,w坐标
// vec4的默认值是0，0，0，1
attribute vec4 a_Position;

void main() {
    gl_Position = a_Position;
}