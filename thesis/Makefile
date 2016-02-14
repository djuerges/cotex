TEXFILES= *.tex
FILE=thesis

pdf: $(FILE).tex
	pdflatex $(FILE).tex
	bibtex $(FILE)
	pdflatex $(FILE).tex
	makeindex $(FILE).nlo -s nomencl.ist -t $(FILE).nlg -o $(FILE).nls;
	pdflatex $(FILE).tex
	cp thesis.pdf thesis-djuerges-`date +%Y-%m-%d`.pdf
	make clean
	ls thesis-djuerges-*.pdf|grep -v thesis-djuerges-`date +%Y-%m-%d`.pdf|xargs rm -rf

bib: $(TEXFILES) bibliography.bib
	make clean
	pdflatex $(FILE).tex
	bibtex $(FILE)
	pdflatex $(FILE).tex
	pdflatex $(FILE).tex

clean:
	rm -f *.dvi *.aux *~ */*~ *.bbl *.blg *.lof *.log *.lot *.toc *.lol *.out *.tdo *.algorithmic *.loa *.exp *.idx *.nlo *.nls *.nlg *.ilg *.glo *.ist *.fls *.fdb_latexmk *.ind
	rm -f $(FILE).p.
