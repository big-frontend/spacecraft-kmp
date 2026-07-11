package com.electrolytej.renderer

import com.electrolytej.vulkan.*
import kotlinx.cinterop.*
import platform.android.*

@OptIn(ExperimentalForeignApi::class)
class VulkanRenderer {
    private var instance: VkInstance? = null
    private var device: VkDevice? = null
    private var physicalDevice: VkPhysicalDevice? = null
    private var surface: VkSurfaceKHR? = null
    private var queue: VkQueue? = null

    fun initVulkan(aNativeWindow: CPointer<ANativeWindow>) {
        memScoped {
            // 1. Create Instance with Android Surface Extensions
            val extensions = allocArrayOf(
                VK_KHR_SURFACE_EXTENSION_NAME.cstr.ptr,
                VK_KHR_ANDROID_SURFACE_EXTENSION_NAME.cstr.ptr
            )

            val appInfo = alloc<VkApplicationInfo>().apply {
                sType = VK_STRUCTURE_TYPE_APPLICATION_INFO
                pApplicationName = "KMPVulkanLive".cstr.ptr
                apiVersion = VK_API_VERSION_1_0.toUInt()
            }

            val createInfo = alloc<VkInstanceCreateInfo>().apply {
                sType = VK_STRUCTURE_TYPE_INSTANCE_CREATE_INFO
                pApplicationInfo = appInfo.ptr
                enabledExtensionCount = 2u
                ppEnabledExtensionNames = extensions
            }

            val instancePtr = alloc<VkInstanceVar>()
            if (vkCreateInstance(createInfo.ptr, null, instancePtr.ptr) != VK_SUCCESS) return
            instance = instancePtr.value

            // 2. Create Android Surface
            val surfaceCreateInfo = alloc<VkAndroidSurfaceCreateInfoKHR>().apply {
                sType = VK_STRUCTURE_TYPE_ANDROID_SURFACE_CREATE_INFO_KHR
                window = aNativeWindow
            }
            val surfacePtr = alloc<VkSurfaceKHRVar>()
            if (vkCreateAndroidSurfaceKHR(instance, surfaceCreateInfo.ptr, null, surfacePtr.ptr) != VK_SUCCESS) return
            surface = surfacePtr.value

            // 3. Pick Physical Device & Logical Device
            val deviceCount = alloc<UIntVar>()
            vkEnumeratePhysicalDevices(instance, deviceCount.ptr, null)
            val devices = allocArray<VkPhysicalDeviceVar>(deviceCount.value.toInt())
            vkEnumeratePhysicalDevices(instance, deviceCount.ptr, devices)
            physicalDevice = devices[0]

            val queueCreateInfo = alloc<VkDeviceQueueCreateInfo>().apply {
                sType = VK_STRUCTURE_TYPE_DEVICE_QUEUE_CREATE_INFO
                queueFamilyIndex = 0u 
                queueCount = 1u
                pQueuePriorities = alloc<FloatVar>().apply { value = 1.0f }.ptr
            }

            val deviceCreateInfo = alloc<VkDeviceCreateInfo>().apply {
                sType = VK_STRUCTURE_TYPE_DEVICE_CREATE_INFO
                queueCreateInfoCount = 1u
                pQueueCreateInfos = queueCreateInfo.ptr
                // Add swapchain extension
                enabledExtensionCount = 1u
                ppEnabledExtensionNames = allocArrayOf(VK_KHR_SWAPCHAIN_EXTENSION_NAME.cstr.ptr)
            }

            val devicePtr = alloc<VkDeviceVar>()
            vkCreateDevice(physicalDevice, deviceCreateInfo.ptr, null, devicePtr.ptr)
            device = devicePtr.value
            
            val queuePtr = alloc<VkQueueVar>()
            vkGetDeviceQueue(device, 0u, 0u, queuePtr.ptr)
            queue = queuePtr.value
        }
    }

    fun cleanup() {
        vkDestroySurfaceKHR(instance, surface, null)
        vkDestroyDevice(device, null)
        vkDestroyInstance(instance, null)
    }
}
