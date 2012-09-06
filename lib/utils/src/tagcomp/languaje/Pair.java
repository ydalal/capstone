package tagcomp.languaje;

import java.io.Serializable;

public class Pair<A, B> implements Serializable
{
	private static final long serialVersionUID = -7759977167008210177L;
	private A a;
	private B b;

	public A getA()
	{
		return a;
	}

	public B getB()
	{
		return b;
	}

	public Pair()
	{
		this.a = null;
		this.b = null;
	}

	public Pair(final A a, final B b)
	{
		this.a = a;
		this.b = b;
	}

	public static <A, B> Pair<A, B> create(A a, B b)
	{
		return new Pair<A, B>(a, b);
	}

	@SuppressWarnings("unchecked")
	public final boolean equals(Object o)
	{
		if (!(o instanceof Pair))
		{
			return false;
		}

		final Pair<?, ?> other = (Pair) o;
		return equal(getA(), other.getA()) && equal(getB(), other.getB());
	}

	public static final boolean equal(Object o1, Object o2)
	{
		if (o1 == null)
		{
			return o2 == null;
		}
		return o1.equals(o2);
	}

	public int hashCode()
	{
		int hA = getA() == null ? 0 : getA().hashCode();
		int hB = getB() == null ? 0 : getB().hashCode();

		return hA + (57 * hB);
	}
}
