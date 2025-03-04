# Ignore inline messages which lay outside a diff's range of PR
github.dismiss_out_of_range_messages

# Make it more obvious that a PR is a work in progress and shouldn't be merged yet
warn("PR is classed as Work in Progress") if github.pr_title.include? "[WIP]"
warn("PR is classed as Work in Progress") if github.pr_title.include? "[wip]"

# Warn when PR has no reviewers
warn("PR has no requested reviewers") if github.pr_json["requested_reviewers"].nil?

# Warn when PR has no assignees
warn("PR has no assignees") if github.pr_json["assignee"].nil?

# Warn when there is a big PR
warn("Big PR") if git.lines_of_code > 1000

# ktlint
checkstyle_format.base_path = Dir.pwd
Dir["**/reports/ktlint/*/*.xml"].each do |file|
  checkstyle_format.report file
end

# android lint
android_lint.skip_gradle_task = true
android_lint.filtering = true
Dir["*/build/reports/lint-results*.xml"].each do |file|
  android_lint.report_file = file
  android_lint.lint(inline_mode: true)
end