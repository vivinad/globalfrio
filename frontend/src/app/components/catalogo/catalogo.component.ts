import { Component, OnInit } from '@angular/core';
import { CotizadorService } from '../../services/cotizador.service';
import { Pagina, ProductoCotizador } from '../../models/cotizador.model';

/**
 * Catálogo de equipos y servicios con paginación (Semana 11: Pageable).
 * El backend devuelve cada página con su metadata y aquí solo se navega entre páginas.
 */
@Component({
  selector: 'app-catalogo',
  templateUrl: './catalogo.component.html',
  styleUrls: ['./catalogo.component.css']
})
export class CatalogoComponent implements OnInit {
  readonly tamanio = 6;
  readonly filtros: { valor: string | null; texto: string }[] = [
    { valor: null, texto: 'Todo' },
    { valor: 'EQUIPO', texto: 'Equipos' },
    { valor: 'SERVICIO', texto: 'Servicios' }
  ];

  categoria: string | null = null;
  pagina: Pagina<ProductoCotizador> | null = null;
  cargando = false;
  error = '';

  constructor(private cotizadorService: CotizadorService) {}

  ngOnInit(): void {
    this.cargar(0);
  }

  filtrar(categoria: string | null): void {
    this.categoria = categoria;
    this.cargar(0);
  }

  cargar(numero: number): void {
    this.cargando = true;
    this.error = '';
    this.cotizadorService.listarProductos(this.categoria, numero, this.tamanio).subscribe({
      next: resultado => {
        this.pagina = resultado;
        this.cargando = false;
      },
      error: e => {
        this.error = e.error?.message ?? 'No se pudo cargar el catálogo. Intenta de nuevo.';
        this.cargando = false;
      }
    });
  }

  get numeros(): number[] {
    return this.pagina ? Array.from({ length: this.pagina.totalPaginas }, (_, i) => i) : [];
  }
}
