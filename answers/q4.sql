select count(id), open_date
from bugs
where open_date between :from_date and :to_date
group by open_date
order by open_date;