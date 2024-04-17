package cn.abridge.springframework.core.io;

/**
 * @Author: lyd
 * @Date: 2024/3/31 21:17
 * @Description: 这是一个资源描述符的接口，它抽象出底层资源的实际类型，例如文件或类路径资源。
 * <p>如果每个资源在物理形态下存在，那么可以为其开启一个输入流，
 * 但是，只有某些资源才能返回URL或文件句柄。具体的行为取决于实现方式。</p>
 */
public interface Resource extends InputStreamSource {
    /**
     * todo: 资源校验是否存在
     * <p>确定此资源是否实际存在于物理形态中。
     * 此方法执行明确的存在性检查，而{@code Resource} 句柄的存在只保证了一个有效的描述符句柄。
     * @return true或者false
     */
    boolean exists();
}
