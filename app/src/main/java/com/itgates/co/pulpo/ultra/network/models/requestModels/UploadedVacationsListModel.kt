package com.itgates.co.pulpo.ultra.network.models.requestModels

import android.content.Context
import okhttp3.MultipartBody
import okhttp3.RequestBody

data class UploadedVacationsListModel(
    val vacations: List<UploadedVacationModel>
) {
    fun getUploadedIdListPart(): List<RequestBody> {
        return vacations.map { model -> model.getUploadedIdPart() }
    }

    fun getUploadedOfflineIdListPart(): List<RequestBody> {
        return vacations.map { model -> model.getUploadedOfflineIdPart() }
    }

    fun getUploadedVacationTypeIdListPart(): List<RequestBody> {
        return vacations.map { model -> model.getUploadedVacationTypeIdPart() }
    }

    fun getUploadedDurationTypeIdListPart(): List<RequestBody> {
        return vacations.map { model -> model.getUploadedDurationTypeIdPart() }
    }

    fun getUploadedShiftIdListPart(): List<RequestBody> {
        return vacations.map { model -> model.getUploadedShiftIdPart() }
    }

    fun getUploadedDateFromListPart(): List<RequestBody> {
        return vacations.map { model -> model.getUploadedDateFromPart() }
    }

    fun getUploadedDateToListPart(): List<RequestBody> {
        return vacations.map { model -> model.getUploadedDateToPart() }
    }

    fun getUploadedNoteListPart(): List<RequestBody> {
        return vacations.map { model -> model.getUploadedNotePart() }
    }

    fun getUploadedTwoDimensionalFileListPart(): List<List<MultipartBody.Part>> {
        return vacations.map { model -> model.getUploadedFileListPart() }
    }
}