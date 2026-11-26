import os
import win32com.client

docx_path = os.path.abspath("Project_Report/ParaBank_Project_Report.docx")
pdf_path = os.path.abspath("Project_Report/ParaBank_Project_Report.pdf")

print(f"Converting:\n  Input: {docx_path}\n  Output: {pdf_path}")

word = win32com.client.DispatchEx("Word.Application")
word.Visible = False
try:
    doc = word.Documents.Open(docx_path)
    # 17 = wdFormatPDF
    doc.SaveAs(pdf_path, FileFormat=17)
    doc.Close()
    print("Conversion completed successfully!")
except Exception as e:
    print(f"Error during conversion: {e}")
finally:
    word.Quit()

