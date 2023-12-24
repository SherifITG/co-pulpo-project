package com.itgates.co.pulpo.ultra.network.models.requestModels

import com.itgates.co.pulpo.ultra.roomDataBase.entity.generalData.Vacation
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.enums.ShiftEnum
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

const val bodyType = "multipart/form-data"

data class UploadedVacationModel(
    val id: Long,
    val offline_id: Long,
    val vacation_type_id: Long,
    val duration_type_id: Int,
    val shift_id: Int,
    val dateFrom: String,
    val dateTo: String,
    val note: String,
    val filesPath: List<String>
) {
    constructor(vacation: Vacation): this (
        vacation.onlineId, vacation.id, vacation.vacationTypeId, vacation.durationType.index.toInt(),
        when (vacation.shift) {
            ShiftEnum.AM_SHIFT -> ShiftEnum.AM_SHIFT.index.toInt()
            ShiftEnum.PM_SHIFT -> ShiftEnum.PM_SHIFT.index.toInt()
            ShiftEnum.OTHER -> ShiftEnum.OTHER.index.toInt()
            else -> ShiftEnum.OTHER.index.toInt()
        },
        vacation.dateFrom, vacation.dateTo, vacation.note, vacation.uriListsInfo.paths
    )

    fun getUploadedIdPart(): RequestBody {
        return RequestBody.create(MediaType.parse(bodyType), id.toString())
    }

    fun getUploadedOfflineIdPart(): RequestBody {
        return RequestBody.create(MediaType.parse(bodyType), offline_id.toString())
    }

    fun getUploadedVacationTypeIdPart(): RequestBody {
        return RequestBody.create(MediaType.parse(bodyType), vacation_type_id.toString())
    }

    fun getUploadedDurationTypeIdPart(): RequestBody {
        return RequestBody.create(MediaType.parse(bodyType), duration_type_id.toString())
    }

    fun getUploadedShiftIdPart(): RequestBody {
        return RequestBody.create(MediaType.parse(bodyType), shift_id.toString())
    }

    fun getUploadedDateFromPart(): RequestBody {
        return RequestBody.create(MediaType.parse(bodyType), dateFrom)
    }

    fun getUploadedDateToPart(): RequestBody {
        return RequestBody.create(MediaType.parse(bodyType), dateTo)
    }

    fun getUploadedNotePart(): RequestBody {
        return RequestBody.create(MediaType.parse(bodyType), note)
    }

    fun getUploadedFileListPart(): List<MultipartBody.Part> {
        return filesPath.map { path ->
            val file = File(path)
            val requestFile = RequestBody.create(MediaType.parse(bodyType), file)
            return@map MultipartBody.Part.createFormData("image", file.name, requestFile)
        }
    }
}