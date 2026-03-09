# 🎧 Hi-Fi Enhancer para LG V50

Aplicación Android que activa el modo de alta impedancia en el LG V50 ThinQ usando comandos `tinymix` para mejorar la calidad de audio.

## ⚠️ ADVERTENCIAS IMPORTANTES

- **Requiere acceso ROOT** (Magisk, SuperSU, KernelSU)
- **Solo para LG V50 ThinQ** - No usar en otros dispositivos
- **Uso bajo tu propio riesgo** - Modificar valores de audio puede causar inestabilidad
- Los ajustes se pierden al reiniciar el dispositivo

## 📋 Requisitos

- Android 9.0 (API 21) o superior
- Dispositivo con root concedido
- LG V50 ThinQ (LM-V500, LM-V500N, etc.)

## 🚀 Instalación

1. Descarga el APK desde [Releases](../../releases)
2. Instala en tu dispositivo
3. Concede permisos de Superusuario cuando se soliciten
4. Presiona "Activar Modo Hi-Fi"

## 🛠️ Compilar desde código fuente

```bash
git clone https://github.com/pipeprogamer89/HiFiEnhancer.git
cd HiFiEnhancer
./gradlew assembleDebug