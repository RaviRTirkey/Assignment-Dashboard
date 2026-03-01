import android.util.Log.e
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class MovementPoint(val timeSec: Float, val angle: Float)
data class HistoryPoint(val date: String, val romAngle: Float)

data class JointPoint(
    val id: String,
    val xFraction: Float, 
    val yFraction: Float, 
    val isSelected: Boolean = false,
    val isMainJoint: Boolean = false 
)


data class DashboardUiState(
    val patientName: String = "Mr. rashi",
    val availableDates: List<String> = listOf("17 Apr 2023", "16 Apr 2023"),
    val selectedDate: String = "17 Apr 2023",
    val availableSessions: List<String> = listOf("session_5", "session_6", "session_7", "session_8"),
    val selectedSession: String = "session_6",

    val isAssisted: Boolean = false,
    val sessionTimeMins: Int = 2,
    val movementScore: Int = 8,
    val successRatePct: Int = 88,

    val currentRomAngle: Int = 20,
    val handJoints: List<JointPoint> = listOf(
        JointPoint(
            "Index_DIP",
            xFraction = 0.38f,
            yFraction = 0.15f,
            isSelected = true,
            isMainJoint = true
        ), 
        JointPoint(
            "Index_PIP",
            xFraction = 0.38f,
            yFraction = 0.32f,
            isSelected = false,
            isMainJoint = false
        ), 
        JointPoint(
            "Index_MCP",
            xFraction = 0.38f,
            yFraction = 0.48f,
            isSelected = false,
            isMainJoint = false
        ),
        JointPoint(
            "Thumb_Tip",
            xFraction = 0.18f,
            yFraction = 0.45f,
            isSelected = true,
            isMainJoint = true
        )  
    ),

    val movementData: List<MovementPoint> = emptyList(),
    val historyData: List<HistoryPoint> = emptyList()
)

class DashboardViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    private val db = FirebaseFirestore.getInstance()

    init {
        fetchSessionData("session_6")
    }

    private fun fetchSessionData(sessionId: String) {
        viewModelScope.launch {
            val document = db.collection("sessions").document(sessionId).get().await()

            try {
                if (document.exists()) {
                    val rawMovementData =
                        document.get("movementData") as? List<Map<String, Any>> ?: emptyList()
                    val parseMovementData = rawMovementData.map { map ->
                        MovementPoint(
                            timeSec = (map["timeSec"] as? Number)?.toFloat() ?: 0f,
                            angle = (map["angle"] as? Number)?.toFloat() ?: 0f
                        )
                    }
    
                    val rawHistoryData =
                        document.get("historyData") as? List<Map<String, Any>> ?: emptyList()
                    val parsedHistoryData = rawHistoryData.map { map ->
                        HistoryPoint(
                            date = (map["date"] as? String)?.replace("\\n", "\n")
                                ?: "", 
                            romAngle = (map["romAngle"] as? Number)?.toFloat() ?: 0f
                        )
                    }
    
                    _uiState.update {
                        it.copy(
                            patientName = document.getString("patientName") ?: "Unknown",
                            selectedDate = document.getString("date") ?: "",
                            selectedSession = sessionId,
                            isAssisted = document.getBoolean("isAssisted") ?: false,
                            sessionTimeMins = (document.getLong("sessionTimeMins") ?: 0).toInt(),
                            movementScore = (document.getLong("movementScore") ?: 0).toInt(),
                            successRatePct = (document.getLong("successRatePct") ?: 0).toInt(),
                            currentRomAngle = (document.getLong("currentRomAngle") ?: 0).toInt(),
                            movementData = parseMovementData,
                            historyData = parsedHistoryData
                        )
                    }
                } else {
                    println("Document does not exist!")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                println("Error fetching data: ${e.message}")
            }

        } 
    }

    fun onSessionChanged(newSessionId: String) {
        _uiState.value = _uiState.value.copy(selectedSession = newSessionId)
        
        fetchSessionData(newSessionId)
    }

    fun onDateSelected(newDate: String) {
        _uiState.value = _uiState.value.copy(selectedDate = newDate)
        
    }
}