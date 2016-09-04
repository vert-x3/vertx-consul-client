key "" {
  policy = "read"
}
key "foo/" {
  policy = "write"
}
key "foo/private/" {
  policy = "deny"
}
