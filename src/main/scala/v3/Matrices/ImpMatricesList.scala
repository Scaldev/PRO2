package PRO2.projet.v3

import fr.istic.scribble.*

object ImpMatricesList extends Matrices {

  opaque type T[Elt] = List[List[Elt]]

  // **************************************************************************** \\
  // *                                                                          * \\
  // *                             FONCTIONS PRIVEES                            * \\
  // *                                                                          * \\
  // **************************************************************************** \\

  /** @param p un entier naturel.
    * @param e un élément.
    * @return la liste de taille p contenant uniquement l'élément e.
    */
  private def init_list[Elt](p: Int, e: Elt): List[Elt] = {
    p match {
      case 0 => Nil
      case _ => e :: init_list(p - 1, e)
    }
  }

  /** @param m une matrice.
    * @param i le numéro d'une ligne.
    * @return Some(l), où l est là i-ème ligne de m (si elle existe), None sinon.
    */
  private def get_matrix_line[Elt](m: T[Elt], i: Int): Option[List[Elt]] = {
    m match {
      case Nil     => None
      case l :: ls => if i == 0 then Some(l) else get_matrix_line(ls, i - 1)
    }
  }
  
  /**
    * @param l une liste.
    * @param e un élément.
    * @param k un entier naturel.
    * @return la liste l avec k fois l'élément e ajouté en tête de liste.
    */
  private def list_of_element[Elt](l: List[Elt], e: Elt, k: Int): List[Elt] = {
    k match {
      case 0 => Nil
      case _ => e :: list_of_element(l, e, k - 1)
    }
  }

  /**
    * @param m une matrice.
    * @param e un élément par défaut.
    * @param k le nombre de lignes à ajouter.
    * @return la matrice m avec k lignes de plus, contenant toutes
    *         uniquement l'élément e.
    */
  private def add_lines[Elt](m: T[Elt], e: Elt, k: Int): T[Elt] = {

    val (_, p) = get_dimensions(m)
    
    k match {
      case 0 => m
      case _ => add_lines(m, e, k - 1) :+ list_of_element(Nil, e, p)
    }
  }

  /**
    * @param m une matrice.
    * @param e un élément par défaut.
    * @param k le nombre de colonnes à ajouter.
    * @return la matrice m avec k colonnes de plus, contenant toutes
    *         uniquement l'élément e.
    */
  private def add_columns[Elt](m: T[Elt], e: Elt, k: Int): T[Elt] = {

    k match {
      case 0 => m
      case _ => m.map(l => l ++ list_of_element(l, e, k))
    }
    
  }

  // **************************************************************************** \\
  // *                                                                          * \\
  // *                            FONCTIONS PUBLIQUES                           * \\
  // *                                                                          * \\
  // **************************************************************************** \\

  /** @param n un entier naturel.
    * @param p un entier naturel.
    * @param e un élément.
    * @return la matrice de taille n * p ne contenant que des éléments e.
    */
  def init_matrix[Elt](n: Int, p: Int, e: Elt): T[Elt] = {

    n match {
      case 0 => Nil
      case _ => init_list(p, e) :: init_matrix(n - 1, p, e)
    }

  }

  /** @param m une matrice.
    * @return le couple (hauteur, largeur) de la matrice.
    */
  def get_dimensions[Elt](m: T[Elt]): (Int, Int) = {

    m match {
      case Nil     => (0, 0)
      case l :: ls => (m.length, l.length)
    }

  }

  /** @param m une matrice de taille n * p.
    * @param i un numéro de ligne.
    * @param j un numéro de colonne.
    * @return Some(e), où e est l'élément de m à la position (i, j)
    *         si une telle position est valide, None sinon.
    */
  def get_element[Elt](m: T[Elt], i: Int, j: Int): Option[Elt] = {

    get_matrix_line(m, i) match {
      case None    => None
      case Some(l) => l.lift(j)
    }

  }

  /** @param m une matrice.
    * @param i le numéro de la ligne.
    * @param j le numéro de la colonne.
    * @param e l'élement.
    * @return la matrice m où si un élément a les coordonnées (i, j), il vaut désormais e.
    */
  def set_element[Elt](m: T[Elt], i: Int, j: Int, e: Elt): T[Elt] = {

    val (n, p) = get_dimensions(m)

    if 0 <= i && i < n && 0 <= j && j < p then
      m.updated(i, m(i).updated(j, e))
    else
      m

  }

  /** @param lines une liste de listes de même taille.
    * @return la matrice formée de la liste de listes, si possible.
    */
  def list_to_matrix[Elt](lines: List[List[Elt]]): T[Elt] = {

    val hasMatrixShape = lines.length == 0 || lines.forall(l => l.length == lines.head.length)

    if !hasMatrixShape then {
      throw Exception("Invalid input: size_order < 1.")
    } else lines

  }

  /** @param lines une liste de listes de même taille.
    * @return la matrice formée à partir de lines.
    */
  def matrix_to_list[Elt](m: T[Elt]): List[Elt] = {
    
    m.foldRight(Nil: List[Elt])((l, acc) => l ++ acc)
  }

  /** @param m une matrice de taille n * p.
    * @param e l'élément comblant les nouvelles cases.
    * @return la matrice m carrée de taille max(n, p), dont les
    *         nouveaux éléments valent colorTransparent.
    */
  def matrix_to_square[Elt](m: T[Elt], e: Elt): T[Elt] = {

    val (n, p) = get_dimensions(m)

    if n > p then add_columns(m, e, n - p)
    else if n < p then add_lines(m, e, p - n)
    else m
  }


}
