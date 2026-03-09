package com.hifienhancer.pipe

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.topjohnwu.superuser.Shell

class MainActivity : AppCompatActivity() {

    private lateinit var btnApply: Button
    private lateinit var tvStatus: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Configuración opcional (solo para logs)
        Shell.enableVerboseLogging = true

        btnApply = findViewById(R.id.btnApply)
        tvStatus = findViewById(R.id.tvStatus)

        btnApply.setOnClickListener {
            applyHiFiSettings()
        }
    }

    private fun applyHiFiSettings() {
        tvStatus.text = "Solicitando permisos root..."

        // 1. Obtener el shell root (esto muestra el diálogo de Superuser si es necesario)
        Shell.getShell { shell ->
            runOnUiThread {
                if (shell.isRoot) {
                    tvStatus.text = "Root concedido. Aplicando ajustes..."
                    // Ejecutamos los comandos en segundo plano
                    executeHiFiCommands()
                } else {
                    tvStatus.text = "Error: Sin acceso root"
                    Toast.makeText(this, "Permiso root denegado", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun executeHiFiCommands() {
        // Usamos un hilo para no bloquear la UI, pero libsu ya ejecuta en segundo plano.
        // Simplemente llamamos a la función que detecta y ejecuta.
        Thread {
            try {
                val tinymixPath = detectTinymixPath()
                val commands = listOf(
                    "$tinymixPath \"HPHL Volume\" 20",
                    "$tinymixPath \"HPHR Volume\" 20",
                    "$tinymixPath \"RX1 Digital Volume\" 40",
                    "$tinymixPath \"RX2 Digital Volume\" 120",
                    "$tinymixPath \"Es9018 HEADSET TYPE\" 3",
                    "$tinymixPath \"Es9018 Master Volume\" 0",
                    "$tinymixPath \"Es9018 AVC Volume\" 0"
                )

                Shell.cmd(*commands.toTypedArray()).submit { result ->
                    runOnUiThread {
                        if (result.isSuccess) {
                            tvStatus.text = "¡Modo Hi-Fi activado! 🎧"
                            Toast.makeText(this@MainActivity, "Éxito", Toast.LENGTH_SHORT).show()
                        } else {
                            tvStatus.text = "Error en la ejecución"
                            Toast.makeText(this@MainActivity, "Revisa logcat", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    tvStatus.text = "Error: ${e.message}"
                    Toast.makeText(this@MainActivity, "Excepción: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }.start()
    }

    private fun detectTinymixPath(): String {
        // Nota: este método se ejecuta en un hilo secundario, OK.
        val result = Shell.cmd("which tinymix").exec()
        return if (result.isSuccess && result.out.isNotEmpty()) {
            result.out[0].trim()
        } else {
            listOf("/system/bin/tinymix", "/vendor/bin/tinymix").firstOrNull {
                Shell.cmd("test -f $it").exec().isSuccess
            } ?: "tinymix"
        }
    }
}