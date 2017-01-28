package de.mpii.clausie;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import edu.stanford.nlp.ling.IndexedWord;


import edu.stanford.nlp.trees.UniversalEnglishGrammaticalRelations;
import edu.stanford.nlp.trees.GrammaticalRelation;


//JEFF MODIFIED 2 lines:
//import edu.stanford.nlp.trees.semgraph.SemanticGraph;
//import edu.stanford.nlp.trees.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.semgraph.SemanticGraph; 
import edu.stanford.nlp.semgraph.SemanticGraphEdge; 


/** This class provides a set of utilities to work with {@link SemanticGraph}
 * For details on the Dependency parser @see <a href="nlp.stanford.edu/software/dependencies_manual.pdf">the Stanford Parser manual  
 * 
 * @date $LastChangedDate: 2013-04-24 11:35:23 +0200 (Wed, 24 Apr 2013) $
 * @version $LastChangedRevision: 739 $ */
public class DpUtils {

	/** Finds the first occurrence of a grammatical relation in a set of edges */
    public static SemanticGraphEdge findFirstOfRelation(List<SemanticGraphEdge> edges,
            GrammaticalRelation rel) {
        for (SemanticGraphEdge e : edges) {
            if (rel.equals(e.getRelation())) {
                return e;
            }
        }
        return null;
    }

    /** Finds the first occurrence of a grammatical relation or its descendants in a set of edges */
    public static SemanticGraphEdge findFirstOfRelationOrDescendent(List<SemanticGraphEdge> edges,
            GrammaticalRelation rel) {
        for (SemanticGraphEdge e : edges) {
            if (rel.isAncestor(e.getRelation())) {
                return e;
            }
        }
        return null;
    }

    /** Finds the first occurrence of a grammatical relation or its descendants for a relative pronoun */
    public static SemanticGraphEdge findDescendantRelativeRelation(SemanticGraph semanticGraph,
            IndexedWord root, GrammaticalRelation rel) {
        List<SemanticGraphEdge> outedges = semanticGraph.getOutEdgesSorted(root);
        for (SemanticGraphEdge e : outedges) {
            if (e.getDependent().tag().charAt(0) == 'W' && rel.isAncestor(e.getRelation())) {
                return e;
            } else
                return findDescendantRelativeRelation(semanticGraph, e.getDependent(), rel);
        }
        return null;
    }

    /** Finds all occurrences of a grammatical relation or its descendants in a set of edges */
    public static List<SemanticGraphEdge> getEdges(List<SemanticGraphEdge> edges,
            GrammaticalRelation rel) {
        List<SemanticGraphEdge> result = new ArrayList<SemanticGraphEdge>();
        for (SemanticGraphEdge e : edges) {
            if (rel.isAncestor(e.getRelation())) {
                result.add(e);
            }
        }
        return result;
    }

    /** Checks if a given grammatical relation is contained in a set of edges */
    public static boolean containsRelation(List<SemanticGraphEdge> edges, GrammaticalRelation rel) {
        return findFirstOfRelation(edges, rel) != null;
    }

    /** Checks if a given edge holds a subject relation*/
    public static boolean isAnySubj(SemanticGraphEdge edge) {
        return UniversalEnglishGrammaticalRelations.SUBJECT.isAncestor(edge.getRelation());
    }

    /** Checks if a given edge holds a nominal subject relation*/
    public static boolean isNsubj(SemanticGraphEdge edge) {
        return UniversalEnglishGrammaticalRelations.NOMINAL_SUBJECT.equals(edge.getRelation());
    }

    /** Checks if a given edge holds a clausal subject relation*/
    public static boolean isCsubj(SemanticGraphEdge edge) {
        return UniversalEnglishGrammaticalRelations.CLAUSAL_SUBJECT.equals(edge.getRelation());
    }

    /** Checks if a given edge holds a clausal passive subject relation*/
    public static boolean isCsubjpass(SemanticGraphEdge edge) {
        return UniversalEnglishGrammaticalRelations.CLAUSAL_PASSIVE_SUBJECT.equals(edge.getRelation());
    }

    /** Checks if a given edge holds a nominal passive subject relation*/
    public static boolean isNsubjpass(SemanticGraphEdge edge) {
        return UniversalEnglishGrammaticalRelations.NOMINAL_PASSIVE_SUBJECT.equals(edge.getRelation());
    }

    /** Checks if a given edge holds an external subject relation of an xcomp relation */
    public static boolean isXsubj(SemanticGraphEdge edge) {
        //JEFF CHANGED 1 line from CONTROLLING_SUBJECT to SEMANTIC_DEPENDENT 
    	//return UniversalEnglishGrammaticalRelations.CONTROLLING_SUBJECT.equals(edge.getRelation());
        return UniversalEnglishGrammaticalRelations.SEMANTIC_DEPENDENT.equals(edge.getRelation());
    }

    /** Checks if a given edge holds an object relation */
    public static boolean isAnyObj(SemanticGraphEdge edge) {
        return UniversalEnglishGrammaticalRelations.OBJECT.isAncestor(edge.getRelation());
    }

    /** Checks if a given edge holds a prepositional object relation*/
    public static boolean isPobj(SemanticGraphEdge edge) {
        //JEFF POBJ now NMOD
    	//return UniversalEnglishGrammaticalRelations.PREPOSITIONAL_OBJECT.equals(edge.getRelation());
        return UniversalEnglishGrammaticalRelations.NOMINAL_MODIFIER.equals(edge.getRelation());
    }

    /** Checks if a given edge holds a direct object relation */
    public static boolean isDobj(SemanticGraphEdge edge) {
        return UniversalEnglishGrammaticalRelations.DIRECT_OBJECT.equals(edge.getRelation());
    }

    /** Checks if a given edge holds an indirect object relation */
    public static boolean isIobj(SemanticGraphEdge edge) {
        return UniversalEnglishGrammaticalRelations.INDIRECT_OBJECT.equals(edge.getRelation());
    }

    /** Checks if a given edge holds a negation relation */
    static boolean isNeg(SemanticGraphEdge edge) {
        return UniversalEnglishGrammaticalRelations.NEGATION_MODIFIER.equals(edge.getRelation());
    }

    /** Checks if a given edge holds the 'dep' relation */
    static boolean isDep(SemanticGraphEdge edge) {
        return edge.toString().equals("dep");
    }

    /** Checks if a given edge holds a phrasal verb particle relation */
    static boolean isPrt(SemanticGraphEdge edge) {
        return UniversalEnglishGrammaticalRelations.PHRASAL_VERB_PARTICLE.equals(edge.getRelation());
    }

    /** Checks if a given edge holds an apposittional relation */
    static boolean isAppos(SemanticGraphEdge edge) {
        return UniversalEnglishGrammaticalRelations.APPOSITIONAL_MODIFIER.equals(edge.getRelation());
    }

    /** Checks if a given edge holds an purpose clause modifier relation */
    public static boolean isPurpcl(SemanticGraphEdge edge) {
    	//JEFF: the "Purpose Clause" is reserved for the term "in order to"; it was retired. There doesn't seem 
    	// to be an equivalent. It's a special case of ADVCL. I'm modifying one line and hard coding to return false:
        //return UniversalEnglishGrammaticalRelations.PURPOSE_CLAUSE_MODIFIER.equals(edge.getRelation());
    	return false;
    }

    /** Checks if a given edge holds a xcomp relation */
    public static boolean isXcomp(SemanticGraphEdge edge) {
        return UniversalEnglishGrammaticalRelations.XCLAUSAL_COMPLEMENT.equals(edge.getRelation());
    }

    /** Checks if a given edge holds a complementizer relation */
    public static boolean isComplm(SemanticGraphEdge edge) {
    	//JEFF: the "Complementizer" was discontinued. It matched words like "that" or "whether". It was moved into "MARK"
    	//I'm going to hardcode it to always return false. 
        //return UniversalEnglishGrammaticalRelations.COMPLEMENTIZER.equals(edge.getRelation());
    	return false; 
    }

    /** Checks if a given edge holds an agent relation */
    public static boolean isAgent(SemanticGraphEdge edge) {
        return UniversalEnglishGrammaticalRelations.AGENT.equals(edge.getRelation());
    }

    /** Checks if a given edge holds an expletive relation */
    public static boolean isExpl(SemanticGraphEdge edge) {
        return UniversalEnglishGrammaticalRelations.EXPLETIVE.equals(edge.getRelation());
    }

    /** Checks if a given edge holds an adjectival complement relation */
    public static boolean isAcomp(SemanticGraphEdge edge) {
    	//JEFF, this is likely wrong, I switched it to ADJECTIVAL_MODIFIER
        //return UniversalEnglishGrammaticalRelations.ADJECTIVAL_COMPLEMENT.equals(edge.getRelation());
        return UniversalEnglishGrammaticalRelations.ADJECTIVAL_MODIFIER.equals(edge.getRelation());
    }

    /** Checks if a given edge holds a prepositional modifier relation */
    public static boolean isAnyPrep(SemanticGraphEdge edge) {
    	//JEFF, no UD equivalent; This is likely wrong. 
        //return UniversalEnglishGrammaticalRelations.PREPOSITIONAL_MODIFIER.isAncestor(edge.getRelation());
        return UniversalEnglishGrammaticalRelations.CASE_MARKER.isAncestor(edge.getRelation());
    }

    /** Checks if a given edge holds a copular relation */
    public static boolean isCop(SemanticGraphEdge edge) {
        return UniversalEnglishGrammaticalRelations.COPULA.equals(edge.getRelation());
    }

    /** Checks if a given edge holds an adverbial clausal relation */
    public static boolean isAdvcl(SemanticGraphEdge edge) {
    	//JEFF modified to allow for UD's 2 types of ADV Clause MOdifiers (one is for copula) 
        //return UniversalEnglishGrammaticalRelations.ADV_CLAUSE_MODIFIER.equals(edge.getRelation());
    	return (UniversalEnglishGrammaticalRelations.CLAUSAL_MODIFIER.equals(edge.getRelation()) ||
    			UniversalEnglishGrammaticalRelations.ADV_CLAUSE_MODIFIER.equals(edge.getRelation())
    			);
    }

    /** Checks if a given edge holds a relative clause modifier relation */
    public static boolean isRcmod(SemanticGraphEdge edge) {
        return UniversalEnglishGrammaticalRelations.RELATIVE_CLAUSE_MODIFIER.equals(edge.getRelation());
    }

    /** Checks if a given edge holds a clausal complement relation */
    public static boolean isCcomp(SemanticGraphEdge edge) {
        return UniversalEnglishGrammaticalRelations.CLAUSAL_COMPLEMENT.equals(edge.getRelation());
    }

    /** Checks if a given edge holds an adverbial modifier relation */
    public static boolean isAdvmod(SemanticGraphEdge edge) {
        return UniversalEnglishGrammaticalRelations.ADVERBIAL_MODIFIER.equals(edge.getRelation());
    }

    /** Checks if a given edge holds an np adverbial modifier relation */
    public static boolean isNpadvmod(SemanticGraphEdge edge) {
        return UniversalEnglishGrammaticalRelations.NP_ADVERBIAL_MODIFIER.equals(edge.getRelation());
    }

    /** Checks if a given edge holds a marker relation */
    public static boolean isMark(SemanticGraphEdge edge) {
        return UniversalEnglishGrammaticalRelations.MARKER.equals(edge.getRelation());
    }

    /** Checks if a given edge holds a propositional complement relation */
    public static boolean isPcomp(SemanticGraphEdge edge) {
    	//JEFF, no equivalent in UD; it looks like they're using a combination of "acl" and "advcl" 
        //return UniversalEnglishGrammaticalRelations.PREPOSITIONAL_COMPLEMENT.equals(edge.getRelation());
        return (UniversalEnglishGrammaticalRelations.ADV_CLAUSE_MODIFIER.equals(edge.getRelation()) ||
        		UniversalEnglishGrammaticalRelations.CLAUSAL_MODIFIER.equals(edge.getRelation())
        		);
    }

    /** Checks if a given edge holds a possession modifier relation */
    public static boolean isPoss(SemanticGraphEdge edge) {
        return UniversalEnglishGrammaticalRelations.POSSESSION_MODIFIER.equals(edge.getRelation());
    }

    /** Checks if a given edge holds a possessive modifier relation */
    public static boolean isPosse(SemanticGraphEdge edge) {
    	//JEFF, modified for a non-exact UD equivalent
        //return UniversalEnglishGrammaticalRelations.POSSESSIVE_MODIFIER.equals(edge.getRelation());
    	return UniversalEnglishGrammaticalRelations.CASE_MARKER.equals(edge.getRelation());
    }

    /** Checks if a given edge holds a participial modifier relation */
    public static boolean isPartMod(SemanticGraphEdge edge) {
    	//JEFF is modifying one line. PARTICIPIAL_MODIFIER was retired. Hard coding to return false. 
        //return UniversalEnglishGrammaticalRelations.PARTICIPIAL_MODIFIER.equals(edge.getRelation());
    	return false; 
    }

    /** Checks if a given edge holds a temporal modifier relation */
    public static boolean isTmod(SemanticGraphEdge edge) {
        return UniversalEnglishGrammaticalRelations.TEMPORAL_MODIFIER.equals(edge.getRelation());
    }

    /** Checks if a given edge holds a conjunct relation */
    public static boolean isAnyConj(SemanticGraphEdge edge) {
        return UniversalEnglishGrammaticalRelations.CONJUNCT.isAncestor(edge.getRelation());
    }

    /** Checks if a given edge holds a preconjunct modifier relation */
    public static boolean isPreconj(SemanticGraphEdge edge) {
        return UniversalEnglishGrammaticalRelations.PRECONJUNCT.equals(edge.getRelation());
    }

    /** Checks if a given edge holds a coordination relation */
    public static boolean isCc(SemanticGraphEdge edge) {
        return UniversalEnglishGrammaticalRelations.COORDINATION.equals(edge.getRelation());
    }

    /** Checks if a given edge holds an auxiliar modifier relation */
    public static boolean isAux(SemanticGraphEdge edge) {
        return UniversalEnglishGrammaticalRelations.AUX_MODIFIER.equals(edge.getRelation());
    }

    /** Checks if a given edge holds an auxiliar passive modifier relation */
    public static boolean isAuxPass(SemanticGraphEdge edge) {
        return UniversalEnglishGrammaticalRelations.AUX_PASSIVE_MODIFIER.equals(edge.getRelation());
    }

    /** Checks if a given edge holds a 'rel' relation */
    public static boolean isRel(SemanticGraphEdge edge) {
        return UniversalEnglishGrammaticalRelations.RELATIVE.equals(edge.getRelation());
    }
    
    /** Checks if a given edge holds a multi word expression relation */
    public static boolean isMwe(SemanticGraphEdge edge) {
		return UniversalEnglishGrammaticalRelations.MULTI_WORD_EXPRESSION.equals(edge.getRelation());
	}
    
    /** Checks if a given edge holds a parataxis relation */
    public static boolean isParataxis(SemanticGraphEdge edge) {
        return UniversalEnglishGrammaticalRelations.PARATAXIS.equals(edge.getRelation());
    }
    
    /** Checks if a given edge holds an infinitival modifier relation */
    public static boolean isInfmod(SemanticGraphEdge edge) {
    	//JEFF no information on this one; hard coding to return false.
		//return UniversalEnglishGrammaticalRelations.INFINITIVAL_MODIFIER.equals(edge.getRelation());
    	return false; 
	}
    
    /** Checks if a given edge holds a predeterminer relation */
    public static boolean isPredet(SemanticGraphEdge edge) {
    	return UniversalEnglishGrammaticalRelations.PREDETERMINER.equals(edge.getRelation());
	}

    /** Removes some edges from the given semantic graph.
     * 
     * This method traverses the semantic graph starting from the given root. An edge is removed if
     * (1) its child appears in <code>excludeVertexes</code>, (2) its relation appears in
     * <code>excludeRelations</code>, or (3) the edge has the root as parent and its relation
     * appears in <code>excludeRelationsTop</code>. */
    public static void removeEdges(SemanticGraph graph, IndexedWord root,
            Collection<IndexedWord> excludeVertexes,
            Collection<GrammaticalRelation> excludeRelations,
            Collection<GrammaticalRelation> excludeRelationsTop) {
        if (!excludeVertexes.contains(root)) {
            List<SemanticGraphEdge> edgesToRemove = new ArrayList<SemanticGraphEdge>();
            subgraph(graph, root, excludeVertexes, excludeRelations, excludeRelationsTop,
                    edgesToRemove);
            for (SemanticGraphEdge edge : edgesToRemove) {
                graph.removeEdge(edge);
            }
        }
    }

    /** Removes some edges from the given semantic graph.
     * 
     * This method traverses the semantic graph starting from the given root. An edge is removed if
     * its child appears in <code>excludeVertexes</code>. */
    public static void removeEdges(SemanticGraph graph, IndexedWord root,
            Collection<IndexedWord> excludeVertexes) {
        removeEdges(graph, root, excludeVertexes, Collections.<GrammaticalRelation> emptySet(),
                Collections.<GrammaticalRelation> emptySet());
    }

    /** Removes some edges from the given semantic graph.
     * 
     * This method traverses the semantic graph starting from the given root. An edge is removed if
     * its relation appears in <code>excludeRelations</code> or the edge has the root as parent and
     * its relation appears in <code>excludeRelationsTop</code>. */
    public static void removeEdges(SemanticGraph graph, IndexedWord root,
            Collection<GrammaticalRelation> excludeRelations,
            Collection<GrammaticalRelation> excludeRelationsTop) {
        removeEdges(graph, root, Collections.<IndexedWord> emptySet(), excludeRelations,
                excludeRelationsTop);
    }

    /** Implementation for
     * {@link #removeEdges(SemanticGraph, IndexedWord, Collection, Collection, Collection)} */
    private static void subgraph(SemanticGraph graph, IndexedWord root,
            Collection<IndexedWord> excludeVertexes,
            Collection<GrammaticalRelation> excludeRelations,
            Collection<GrammaticalRelation> excludeRelationsTop,
            Collection<SemanticGraphEdge> edgesToRemove) {
        List<SemanticGraphEdge> edges = graph.getOutEdgesSorted(root);
        for (SemanticGraphEdge e : edges) {
            IndexedWord child = e.getDependent();
            if (excludeVertexes.contains(child) || excludeRelations.contains(e.getRelation())
                    || excludeRelationsTop.contains(e.getRelation())) {
                edgesToRemove.add(graph.getEdge(root, child));
            } else {
                subgraph(graph, child, excludeVertexes, excludeRelations,
                        Collections.<GrammaticalRelation> emptySet(), edgesToRemove);
            }
        }
    }

    /** Disconnects independent clauses by removing the edge representing the coordinating conjunction */
    public static void disconectClauses(SemanticGraph graph, Constituent constituent) {
        List<SemanticGraphEdge> outedges = graph
                .getOutEdgesSorted(((IndexedConstituent) constituent).getRoot());
        for (int i = 0; i < outedges.size(); i++) {
            SemanticGraphEdge e = outedges.get(i);
            if (DpUtils.isAnyConj(e)) {
                IndexedWord child = e.getDependent();
                List<SemanticGraphEdge> outNewRoot = graph.getOutEdgesSorted(child);
                SemanticGraphEdge sub = DpUtils.findFirstOfRelationOrDescendent(outNewRoot,
                        UniversalEnglishGrammaticalRelations.SUBJECT);
                if (sub != null)
                    graph.removeEdge(e);
            }
        }
    }

    /** Return a set of vertexes to be excluded according to a given collection of grammatical relations */
    public static Set<IndexedWord> exclude(SemanticGraph semanticGraph,
            Collection<GrammaticalRelation> rels, IndexedWord root) {
        Set<IndexedWord> exclude = new TreeSet<IndexedWord>();
        List<SemanticGraphEdge> outedges = semanticGraph.getOutEdgesSorted(root);
        for (SemanticGraphEdge edge : outedges) {
            if (containsAncestor(rels, edge)) {
                exclude.add(edge.getDependent());
            }
        }
        return exclude;
    }

    /** Check if an edge is descendant of any grammatical relation in the given set */
    private static boolean containsAncestor(Collection<GrammaticalRelation> rels,
            SemanticGraphEdge edge) {
        for (GrammaticalRelation rel : rels) {
            if (rel.isAncestor(edge.getRelation()))
                return true;
        }
        return false;
    }
   	
}
