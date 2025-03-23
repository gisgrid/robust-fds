
/* ============================== */
/* vpc/outputs.tf                */
/* ============================== */

output "vpc_id" {
  value = aws_vpc.this.id
}

output "private_subnet_ids" {
  value = [
    aws_subnet.private_1.id,
    aws_subnet.private_2.id
  ]
}
