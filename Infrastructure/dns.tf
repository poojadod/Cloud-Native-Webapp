resource "aws_route53_record" "dev_record" {
  zone_id = var.route53_zone_id
  name    = "${var.aws_profile}.${var.domain_name}"
  type    = "A"

  alias {
    name                   = aws_lb.web_alb.dns_name
    zone_id                = aws_lb.web_alb.zone_id
    evaluate_target_health = true
  }
}

