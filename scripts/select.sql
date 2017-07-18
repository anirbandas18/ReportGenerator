select * from profile order by name asc;

select * from FIELD_PERMISSIONS order by name asc;

select * from FIELD_PERMISSIONS where name like 'Standard.profile' order by field asc;

select distinct name from FIELD_PERMISSIONS order by name asc;

select distinct field from field_permissions order by field asc;

select distinct p.name profile, count(f.field) no_of_fields from profile p, field_permissions f where p.name = f.name group by p.name order by p.name asc; 

select p.name profile, f.field field, f.readable readable, f.editable editable from profile p, field_permissions f where p.name like 'Standard.profile' and p.name = f.name group by p.name, f.field, f.readable, f.editable order by p.name asc;

select * from field_permissions f left join profile p on f.name = p.name;