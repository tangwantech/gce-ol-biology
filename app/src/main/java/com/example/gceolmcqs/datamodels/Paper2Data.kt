package com.example.gceolmcqs.datamodels

data class Paper2Data(val subjects: List<Paper2Subject>)
data class Paper2Subject(val title: String, val examTypes: List<Paper2ExamType>)
data class Paper2ExamType(val title: String, val examItems: List<Paper2ExamItem>)
data class Paper2ExamItem(val title: String, val fileName: String)
