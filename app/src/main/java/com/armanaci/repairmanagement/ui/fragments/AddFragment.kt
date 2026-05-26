package com.armanaci.repairmanagement.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.armanaci.repairmanagement.R
import com.armanaci.repairmanagement.data.model.Repair
import com.armanaci.repairmanagement.databinding.FragmentAddBinding
import com.armanaci.repairmanagement.ui.viewmodel.RepairViewModel
import com.armanaci.repairmanagement.utils.CodeGenerator

class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RepairViewModel by activityViewModels()
    private var currentCode = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupStatusSpinner()
        generateNewCode()
        setupButtons()
    }

    private fun setupStatusSpinner() {
        val statuses = arrayOf("منتظر", "در حال تعمیر", "تمام شده")
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            statuses
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerStatus.adapter = adapter
    }

    private fun generateNewCode() {
        currentCode = CodeGenerator.generateUniqueCode()
        binding.tvCode.text = currentCode
    }

    private fun setupButtons() {
        binding.btnRefreshCode.setOnClickListener {
            generateNewCode()
        }

        binding.btnCopyCode.setOnClickListener {
            val clipboard = requireContext().getSystemService(android.content.Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
            val clip = android.content.ClipData.newPlainText("Code", currentCode)
            clipboard.setPrimaryClip(clip)
            android.widget.Toast.makeText(
                requireContext(),
                "کد کپی شد: $currentCode",
                android.widget.Toast.LENGTH_SHORT
            ).show()
        }

        binding.btnSave.setOnClickListener {
            saveRepair()
        }
    }

    private fun saveRepair() {
        val mobileModel = binding.etMobileModel.text.toString().trim()
        val ownerName = binding.etOwnerName.text.toString().trim()
        val ownerPhone = binding.etOwnerPhone.text.toString().trim()
        val serialNumber = binding.etSerialNumber.text.toString().trim()
        val problemDescription = binding.etProblemDescription.text.toString().trim()
        val status = binding.spinnerStatus.selectedItem.toString()
        val costText = binding.etRepairCost.text.toString().trim()
        val notes = binding.etNotes.text.toString().trim()

        if (validateInputs(mobileModel, ownerName, ownerPhone, serialNumber, problemDescription)) {
            val repairCost = costText.toDoubleOrNull() ?: 0.0

            val repair = Repair(
                uniqueCode = currentCode,
                mobileModel = mobileModel,
                ownerName = ownerName,
                ownerPhone = ownerPhone,
                serialNumber = serialNumber,
                problemDescription = problemDescription,
                status = status,
                repairCost = repairCost,
                notes = notes
            )

            viewModel.insertRepair(repair)
            clearForm()
            android.widget.Toast.makeText(
                requireContext(),
                "تعمیر با موفقیت ثبت شد",
                android.widget.Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun validateInputs(
        model: String,
        name: String,
        phone: String,
        serial: String,
        problem: String
    ): Boolean {
        if (model.isEmpty()) {
            binding.etMobileModel.error = "مدل موبایل را وارد کنید"
            return false
        }
        if (name.isEmpty()) {
            binding.etOwnerName.error = "نام مالک را وارد کنید"
            return false
        }
        if (phone.isEmpty()) {
            binding.etOwnerPhone.error = "شماره تماس را وارد کنید"
            return false
        }
        if (serial.isEmpty()) {
            binding.etSerialNumber.error = "شماره سریال را وارد کنید"
            return false
        }
        if (problem.isEmpty()) {
            binding.etProblemDescription.error = "توضیح مشکل را وارد کنید"
            return false
        }
        return true
    }

    private fun clearForm() {
        binding.etMobileModel.text.clear()
        binding.etOwnerName.text.clear()
        binding.etOwnerPhone.text.clear()
        binding.etSerialNumber.text.clear()
        binding.etProblemDescription.text.clear()
        binding.etRepairCost.text.clear()
        binding.etNotes.text.clear()
        binding.spinnerStatus.setSelection(0)
        generateNewCode()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
