package com.villalobos.caballoapp.ui.tutorial

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.villalobos.caballoapp.R
import com.villalobos.caballoapp.util.ErrorHandler
import com.villalobos.caballoapp.util.ImageAnimationHelper
import com.villalobos.caballoapp.databinding.FragmentTutorialPasoBinding

class TutorialPasoFragment : Fragment() {

    private var _binding: FragmentTutorialPasoBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val ARG_PASO = "tutorial_paso"

        fun newInstance(paso: TutorialPaso): TutorialPasoFragment {
            val fragment = TutorialPasoFragment()
            val args = Bundle()
            args.putSerializable(ARG_PASO, paso)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTutorialPasoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val paso = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getSerializable(ARG_PASO, TutorialPaso::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getSerializable(ARG_PASO) as? TutorialPaso
        }
        paso?.let { configurarPaso(it) }
    }

    private fun configurarPaso(paso: TutorialPaso) {
        ErrorHandler.safeExecute(
            context = requireContext(),
            errorType = ErrorHandler.ErrorType.UNKNOWN_ERROR,
            errorMessage = "Error al configurar paso del tutorial"
        ) {
            val textColor = ContextCompat.getColor(requireContext(), R.color.text_primary)
            val titleColor = ContextCompat.getColor(requireContext(), R.color.primary_brown)
            
            // Configurar contenido básico
            binding.tvTituloPaso.text = paso.titulo
            binding.tvTituloPaso.setTextColor(titleColor)
            binding.tvDescripcionPaso.text = paso.descripcion
            binding.tvDescripcionPaso.setTextColor(textColor)
            binding.imgPasoTutorial.setImageResource(paso.imagen)

            // Animar la imagen del tutorial
            ImageAnimationHelper.animateTutorialImage(binding.imgPasoTutorial)

            // Configurar características si las tiene
            if (paso.mostrarCaracteristicas && paso.caracteristicas.isNotEmpty()) {
                binding.layoutCaracteristicas.visibility = View.VISIBLE
                binding.layoutCaracteristicas.removeAllViews()

                // Agregar cada característica como TextView
                paso.caracteristicas.forEach { caracteristica ->
                    val textView = TextView(requireContext()).apply {
                        text = caracteristica
                        textSize = 14f
                        setTextColor(textColor)
                        setPadding(0, 0, 0, 16)
                        setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.ic_star_gold, 0, 0, 0
                        )
                        compoundDrawablePadding = 16
                    }
                    binding.layoutCaracteristicas.addView(textView)
                }
            } else {
                binding.layoutCaracteristicas.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 