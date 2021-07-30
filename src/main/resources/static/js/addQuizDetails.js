function addQuestion() {
    const div = document.createElement('div');


    div.innerHTML = `
    <input type="text" name="answer" value="" />
    <label> 
      <input type="checkbox" name="iscorect" value="1" /> Checked? 
    </label>


    <div class="mb-3 row">
				<label class="col-sm-2 col-form-label text-md-end">Question
					Name</label>
				<div class="col-sm-6">
					<input type="text" class="form-control" required th:field="*{name}">
				</div>
			</div>
  `;

    document.getElementById('questionForm').appendChild(div);
}